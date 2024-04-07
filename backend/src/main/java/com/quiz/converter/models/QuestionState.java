package com.quiz.converter.models;

import com.quiz.converter.models.enums.ParagraphType;
import com.quiz.converter.models.enums.QuestionErrorType;
import com.quiz.converter.models.enums.QuestionType;
import com.quiz.converter.models.enums.QuestionWarningType;
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
    private ParagraphType previousParagraphType = ParagraphType.EMPTY_TEXT;
    private List<QuestionErrorType> errors = new ArrayList<>();
    private List<QuestionWarningType> warnings = new ArrayList<>();

    public void cleanState() {
        name = "";
        defaultFeedback = "";
        type = QuestionType.UNKNOWN;
        description = new QuestionDescription();
        answerOptions = new ArrayList<>();
        previousParagraphType = ParagraphType.EMPTY_TEXT;
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    public Question createQuestion() {
        return new Question(name, defaultFeedback, type, description, answerOptions, errors, warnings);
    }
}
