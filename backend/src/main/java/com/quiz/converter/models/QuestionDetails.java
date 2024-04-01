package com.quiz.converter.models;

import java.util.List;

public record QuestionDetails(
        int questionCount,
        int answersCount,
        int picturesCount,
        List<QuestionError> questionErrors
) {
}
