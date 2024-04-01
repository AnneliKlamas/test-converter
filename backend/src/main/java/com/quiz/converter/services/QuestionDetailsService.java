package com.quiz.converter.services;

import com.quiz.converter.models.Question;
import com.quiz.converter.models.QuestionDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionDetailsService {
    public QuestionDetails getQuestionDetails(List<Question> questions) {
        return new QuestionDetails(1, 1, 1, Collections.emptyList());
    }
}
