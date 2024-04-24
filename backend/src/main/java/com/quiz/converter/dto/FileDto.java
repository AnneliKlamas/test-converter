package com.quiz.converter.dto;

import com.quiz.converter.models.QuizDetails;

public class FileDto {
    private String fileName;
    private String file;
    private QuizDetails details;

    public FileDto(String fileName, String file, QuizDetails details) {
        this.fileName = fileName;
        this.file = file;
        this.details = details;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public QuizDetails getDetails() {
        return details;
    }

    public void setDetails(QuizDetails details) {
        this.details = details;
    }
}