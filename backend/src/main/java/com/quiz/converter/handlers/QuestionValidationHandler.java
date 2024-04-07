package com.quiz.converter.handlers;

import com.quiz.converter.models.Answer;
import com.quiz.converter.models.QuestionState;
import com.quiz.converter.models.enums.QuestionErrorType;
import com.quiz.converter.models.enums.QuestionType;


public class QuestionValidationHandler {
    private final QuestionState state;

    public QuestionValidationHandler(QuestionState state) {
        this.state = state;
    }

    public void validateQuestion() {
        if (state.getName().isEmpty()) {
            state.getErrors().add(QuestionErrorType.NO_ANSWER_OPTIONS_FOUND);
        }
        var correctAnswerCount = state.getAnswerOptions().stream().filter(Answer::isCorrect).count();
        if (state.getType().equals(QuestionType.SINGLE_CHOICE) && correctAnswerCount > 1) {
            state.getErrors().add(QuestionErrorType.ANSWER_OPTIONS_DONT_MATCH_TYPE);
        } else if (correctAnswerCount == 0) {
            state.getErrors().add(QuestionErrorType.NO_CORRECT_ANSWER_FOUND);
        }
        if (state.getType().equals(QuestionType.UNKNOWN)) {
            state.getErrors().add(QuestionErrorType.UNKNOWN_QUESTION_TYPE);
        }
    }
}
