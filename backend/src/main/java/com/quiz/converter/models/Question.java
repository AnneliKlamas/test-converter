package com.quiz.converter.models;

import com.quiz.converter.models.enums.QuestionType;

import java.util.List;

public record Question(
        String name,
        String feedback,
        QuestionType type,
        QuestionDescription description,
        List<Answer> answerOptions) {
}
