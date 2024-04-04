package com.quiz.converter.handlers;

import com.quiz.converter.models.Question;
import com.quiz.converter.models.QuestionState;
import com.quiz.converter.models.enums.QuestionType;
import com.quiz.converter.models.enums.QuestionWarningType;

import java.util.Optional;

public class QuestionHandler {
    private final QuestionState state;

    public QuestionHandler(QuestionState state) {
        this.state = state;
    }

    public Optional<Question> handleQuestion(String text) {
        Optional<Question> question = Optional.empty();
        if (!state.getAnswerOptions().isEmpty()) {
            var validationHandler = new QuestionValidationHandler(state);
            validationHandler.validateQuestion();
            question = Optional.of(state.createQuestion());
            state.cleanState();
        }
        addQuestionType(text);
        addQuestionName(text);
        return question;
    }

    private void addQuestionName(String text) {
        try {
            int endOfNameIndex = !text.contains("-") ? text.indexOf("–") : text.indexOf("-");
            state.setName(text.substring(0, endOfNameIndex));
        } catch (Exception e) {
            state.setName(text);
            state.getWarnings().add(QuestionWarningType.CHECK_QUESTION_NAME);
        }
    }

    private void addQuestionType(String text) {
        var lowerCaseText = text.toLowerCase();
        if (lowerCaseText.contains("single choice")) { //TODO: add regex here, as there can be multiple whitespaces between "multiple" and "choice"
            state.setType(QuestionType.SINGLE_CHOICE);
        } else if (lowerCaseText.contains("multiple choice") || lowerCaseText.contains("checkbox")) {
            state.setType(QuestionType.MULTIPLE_CHOICE);
        } else {
            state.setType(QuestionType.UNKNOWN);
        }
    }
}
