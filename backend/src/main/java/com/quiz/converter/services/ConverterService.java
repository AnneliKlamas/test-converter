package com.quiz.converter.services;

import com.quiz.converter.models.QuestionDetails;
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
    QuestionDetailsService questionDetailsService;

    public Pair<byte[], QuestionDetails> convertDocToMoodle(MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        var questions = fileUploadService.convertDocToQuestion(file);
        return Pair.create(moodleXmlCreator.createMoodleXml(questions), questionDetailsService.getQuestionDetails(questions));
    }

    public byte[] convertDocToCoursera(MultipartFile file) throws IOException {
        var questions = fileUploadService.convertDocToQuestion(file);
        return courseraDocxCreator.createCourseraDocx(questions);
    }
}
