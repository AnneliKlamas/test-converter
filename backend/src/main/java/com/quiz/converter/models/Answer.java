package com.quiz.converter.models;

import java.util.List;

public record Answer(String text, String feedback, boolean isCorrect, List<String> pictures) {
}
