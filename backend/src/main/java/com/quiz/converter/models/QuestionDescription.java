package com.quiz.converter.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class QuestionDescription {
    private String text;
    private List<String> pictures;

    public QuestionDescription(String text, List<String> pictures) {
        this.text = text;
        this.pictures = pictures;
    }
}
