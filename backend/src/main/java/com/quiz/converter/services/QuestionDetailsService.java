package com.quiz.converter.services;

import com.quiz.converter.models.Question;
import com.quiz.converter.models.QuestionDetails;
import com.quiz.converter.models.enums.QuestionErrorType;
import com.quiz.converter.models.enums.QuestionType;
import com.quiz.converter.models.enums.QuestionWarningType;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuestionDetailsService {
    public QuestionDetails getQuestionDetails(List<Question> questions) {
        var answerCount = 0;
        var questionPicturesCount = 0;
        var answerPicturesCount = 0;
        var questionTypeCounts = new HashMap<QuestionType, Integer>();
        var questionCount = 0;
        var skippedQuestions = 0;
        var errors = new HashMap<String, List<QuestionErrorType>>();
        var warnings = new HashMap<String, List<QuestionWarningType>>();

        for (var question : questions) {
            if (!question.warnings().isEmpty()) {
                addWarnings(warnings, question);
            }

            if (question.errors().isEmpty()) {
                questionCount += 1;
            } else {
                skippedQuestions += 1;
                addErrors(errors, question);
                continue;
            }

            answerCount += question.answerOptions().size();
            questionPicturesCount += question.description().getPictures().size();
            var typeCount = questionTypeCounts.getOrDefault(question.type(), 0);
            questionTypeCounts.put(question.type(), typeCount + 1);
            for (var answer : question.answerOptions()) {
                answerPicturesCount += answer.getPictures().size();
            }
        }
        return new QuestionDetails(questionCount, questionTypeCounts, answerCount, questionPicturesCount, answerPicturesCount, errors, warnings, skippedQuestions);
    }

    private void addErrors(HashMap<String, List<QuestionErrorType>> errors, Question question) {
        var questionErrors = errors.getOrDefault(question.name(), new ArrayList<>());
        questionErrors.addAll(question.errors());
        errors.put(question.name(), questionErrors);
    }

    private void addWarnings(HashMap<String, List<QuestionWarningType>> warnings, Question question) {
        var questionWarnings = warnings.getOrDefault(question.name(), new ArrayList<>());
        questionWarnings.addAll(question.warnings());
        warnings.put(question.name(), questionWarnings);
    }
}
