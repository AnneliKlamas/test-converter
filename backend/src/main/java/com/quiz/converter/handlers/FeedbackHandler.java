package com.quiz.converter.handlers;

import com.quiz.converter.models.enums.ParagraphType;
import com.quiz.converter.models.QuestionState;

public class FeedbackHandler {
    private final QuestionState state;

    public FeedbackHandler(QuestionState state) {
        this.state = state;
    }

    public void add(String text, ParagraphType paragraphType) {
        if (paragraphType == ParagraphType.DEFAULT_FEEDBACK) {
            state.setDefaultFeedback(text.toLowerCase().replace("default feedback:", "").strip());
        } else if (paragraphType == ParagraphType.FEEDBACK) {
            state.setQuestionFeedbackText(text.toLowerCase().replace("feedback", "").strip().replace(":", "").strip());
        }
    }
}
