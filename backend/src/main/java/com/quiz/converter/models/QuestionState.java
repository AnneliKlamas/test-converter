package com.quiz.converter.models;

import com.quiz.converter.models.enums.ParagraphType;
import com.quiz.converter.models.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionState {
    private String name = "";
    private String defaultFeedback = "";
    private QuestionType type = QuestionType.UNKNOWN;
    private QuestionDescription description = new QuestionDescription();
    private List<Answer> answerOptions = new ArrayList<>();
    private List<String> descriptionPictures = new ArrayList<>();
    private ParagraphType previousParagraphType = ParagraphType.EMPTY_TEXT;
    private String questionFeedbackText = "";

    public void cleanState() {
        name = "";
        defaultFeedback = "";
        type = QuestionType.UNKNOWN;
        description = new QuestionDescription();
        answerOptions = new ArrayList<>();
        descriptionPictures = new ArrayList<>();
        previousParagraphType = ParagraphType.EMPTY_TEXT;
        questionFeedbackText = "";
    }

    public Question createQuestion() {
        return new Question(name, defaultFeedback, type, description, answerOptions);
    }
}
