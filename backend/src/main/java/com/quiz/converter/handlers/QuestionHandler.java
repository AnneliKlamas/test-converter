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
        addQuestionOptions(text);
        return question;
    }

    private void addQuestionOptions(String text) {
        var lowerCaseText = text.toLowerCase();
        if (lowerCaseText.matches(".*(no)\s*(shuffle).*")) {
            state.setShuffle(false);
        } else if (lowerCaseText.matches(".*(shuffle).*")) {
            state.setShuffle(true);
        }
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
        if (lowerCaseText.matches(".*(single)\s*(choice).*")) {
            state.setType(QuestionType.SINGLE_CHOICE);
        } else if (lowerCaseText.matches(".*(multiple)\s*(choice).*") || lowerCaseText.contains("checkbox")) {
            state.setType(QuestionType.MULTIPLE_CHOICE);
        } else if (lowerCaseText.matches(".*(text)\s*(match).*") || lowerCaseText.matches(".*(short)\s*(answer).*")) {
            state.setType(QuestionType.TEXT_MATCH);
        } else {
            state.setType(QuestionType.UNKNOWN);
        }
    }
}
