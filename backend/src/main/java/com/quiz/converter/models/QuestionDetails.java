package com.quiz.converter.models;

import com.quiz.converter.models.enums.QuestionErrorType;
import com.quiz.converter.models.enums.QuestionType;
import com.quiz.converter.models.enums.QuestionWarningType;

import java.util.List;
import java.util.Map;

public record QuestionDetails(
        int questionCount,
        Map<QuestionType, Integer> questionConfigDetails,
        int answersCount,
        int questionPicturesCount,
        int answerPictureCount,
        Map<String, List<QuestionErrorType>> questionErrors,
        Map<String, List<QuestionWarningType>> questionWarnings,
        int skippedQuestions
) {
}
