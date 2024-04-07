package com.quiz.converter.services;

import com.quiz.converter.components.QuizDetailsComponent;
import com.quiz.converter.models.QuizDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ConverterService {
    FileUploadService fileUploadService;
    MoodleXmlCreatorService moodleXmlCreator;
    CourseraDocxCreatorService courseraDocxCreator;
    QuizDetailsComponent quizDetailsComponent;

    public Pair<byte[], QuizDetails> convertDocToMoodle(MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        var questions = fileUploadService.convertDocToQuestion(file);
        var questionsWithoutErrors = questions.stream().filter(q -> q.errors().isEmpty()).toList();
        return Pair.create(moodleXmlCreator.createMoodleXml(questionsWithoutErrors), quizDetailsComponent.getQuestionDetails(questions));
    }

    public Pair<byte[], QuestionDetails> convertDocToCoursera(MultipartFile file) throws IOException {
        var questions = fileUploadService.convertDocToQuestion(file);
        var questionsWithoutErrors = questions.stream().filter(q -> q.errors().isEmpty()).toList();
        return Pair.create(courseraDocxCreator.createCourseraDocx(questionsWithoutErrors), questionDetailsComponent.getQuestionDetails(questions));
    }
}
