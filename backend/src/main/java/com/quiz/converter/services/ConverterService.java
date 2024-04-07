package com.quiz.converter.services;

import com.quiz.converter.components.QuizDetailsComponent;
import com.quiz.converter.models.QuizDetails;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Service
public class ConverterService {
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    MoodleXmlCreatorService moodleXmlCreator;
    @Autowired
    CourseraDocxCreatorService courseraDocxCreator;
    @Autowired
    QuizDetailsComponent quizDetailsComponent;

    public Pair<byte[], QuizDetails> convertDocToMoodle(MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        var questions = fileUploadService.convertDocToQuestion(file);
        var questionsWithoutErrors = questions.stream().filter(q -> q.errors().isEmpty()).toList();
        return Pair.create(moodleXmlCreator.createMoodleXml(questionsWithoutErrors), quizDetailsComponent.getQuestionDetails(questions));
    }

    public Pair<byte[], QuizDetails> convertDocToCoursera(MultipartFile file) throws IOException {
        var questions = fileUploadService.convertDocToQuestion(file);
        var questionsWithoutErrors = questions.stream().filter(q -> q.errors().isEmpty()).toList();
        return Pair.create(courseraDocxCreator.createCourseraDocx(questionsWithoutErrors), quizDetailsComponent.getQuestionDetails(questions));
    }
}
