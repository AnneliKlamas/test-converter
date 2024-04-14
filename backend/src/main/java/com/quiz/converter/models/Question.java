package com.quiz.converter.models;

import com.quiz.converter.models.enums.QuestionErrorType;
import com.quiz.converter.models.enums.QuestionType;
import com.quiz.converter.models.enums.QuestionWarningType;

import java.util.List;

public record Question(
        String name,
        String defaultFeedback,
        QuestionType type,
        QuestionDescription description,
        List<Answer> answerOptions,
        List<QuestionErrorType> errors,
        List<QuestionWarningType> warnings,
        boolean shuffle,
        boolean partialCredit) {
}
