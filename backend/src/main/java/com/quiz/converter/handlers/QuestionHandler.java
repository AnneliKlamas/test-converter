package com.quiz.converter.handlers;

import com.quiz.converter.models.Question;
import com.quiz.converter.models.QuestionState;
import com.quiz.converter.models.enums.QuestionType;

import java.util.Optional;

public class QuestionHandler {
    private final QuestionState state;

    public QuestionHandler(QuestionState state) {
        this.state = state;
    }

    public Optional<Question> handleQuestion(String text) {

        if (!state.getAnswerOptions().isEmpty()) {
            var question = state.createQuestion();
            state.cleanState();
            return Optional.of(question);
        }

        if (text.toLowerCase().contains("single choice")) {
            state.setType(QuestionType.SINGLE_CHOICE);
        }

        try {
            int endOfNameIndex = !text.contains("-") ? text.indexOf("–") : text.indexOf("-");
            state.setName(text.substring(0, endOfNameIndex));
        } catch (Exception e) {
            state.setName(text);
        }
        return Optional.empty();
    }
}
