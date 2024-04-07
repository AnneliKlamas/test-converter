package com.quiz.converter.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class QuestionDescription {
    private String text;
    private List<Picture> pictures;

    public QuestionDescription(String text, List<Picture> pictures) {
        this.text = text;
        this.pictures = pictures;
    }
}
