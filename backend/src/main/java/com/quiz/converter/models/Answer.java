package com.quiz.converter.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@RequiredArgsConstructor
public class Answer {
    private String text;
    private Optional<String> feedback;
    private boolean isCorrect;
    private List<Picture> pictures;

    public Answer(String text, Optional<String> feedback, boolean isCorrect, List<Picture> pictures) {
        this.text = text;
        this.feedback = feedback;
        this.isCorrect = isCorrect;
        this.pictures = pictures;
    }
}
