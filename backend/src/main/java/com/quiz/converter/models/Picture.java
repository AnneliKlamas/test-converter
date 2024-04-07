package com.quiz.converter.models;

import org.apache.poi.common.usermodel.PictureType;

public record Picture(String base64, double width, double height, String name, PictureType type) {
}
