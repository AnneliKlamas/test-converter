package com.quiz.converter.handlers;

import com.quiz.converter.models.enums.ParagraphType;
import com.quiz.converter.models.QuestionState;

import java.util.Optional;

public class FeedbackHandler {
    private final QuestionState state;

    public FeedbackHandler(QuestionState state) {
        this.state = state;
    }

    public void add(String text, ParagraphType paragraphType) {
        if (paragraphType.equals(ParagraphType.DEFAULT_FEEDBACK)) {
            state.setDefaultFeedback(text.toLowerCase().replace("default feedback:", "").strip());
        } else if (paragraphType.equals(ParagraphType.FEEDBACK)) {
            var feedbackText = text.toLowerCase().replace("feedback", "").strip().replace(":", "").strip();
            state.getAnswerOptions().get(state.getAnswerOptions().size() - 1).setFeedback(Optional.of(feedbackText));
        }
    }
}
