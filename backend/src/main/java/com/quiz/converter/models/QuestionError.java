package com.quiz.converter.models;

public record QuestionError(
        String questionName,
        String errorMessage
) {
}
