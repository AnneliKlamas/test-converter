package com.quiz.converter.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class QuestionDescription {
    private List<String> texts = new ArrayList<>();
    private List<Picture> pictures = new ArrayList<>();

    public QuestionDescription(List<String> texts, List<Picture> pictures) {
        this.texts = texts;
        this.pictures = pictures;
    }
}
