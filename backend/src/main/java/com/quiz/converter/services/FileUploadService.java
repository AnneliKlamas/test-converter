package com.quiz.converter.services;

import com.quiz.converter.models.Answer;
import com.quiz.converter.models.Question;
import com.quiz.converter.models.enums.QuestionType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FileUploadService {

    public List<Question> convertDocToQuestion(MultipartFile file) throws IOException {
        XWPFDocument docx = new XWPFDocument(file.getInputStream());

        //using XWPFWordExtractor Class
        QuestionType questionType = QuestionType.UNKNOWN;
        var questions = new ArrayList<Question>();
        var previousQuestionType = false;
        var question = "";
        var answers = new ArrayList<Answer>();
        var questionName = "";
        var defaultFeedback = "";
        var questionFeedbackText = "";
        var paragraphPictures = new ArrayList<>();

        for (XWPFParagraph paragraph : docx.getParagraphs()) { //TODO: Maybe allow multi paragraph question text

            var text1 = paragraph.getParagraphText();
            var text2 = paragraph.getText();

            var runsWithPictures = paragraph.getRuns().stream().filter(r -> !r.getEmbeddedPictures().isEmpty()).toList();

            if (runsWithPictures.size() != 0) {
                for (var run : runsWithPictures) {
                    var pictures = run.getEmbeddedPictures();
                    for (var picture : pictures) {
                        paragraphPictures.add(Base64.getEncoder().encodeToString(picture.getPictureData().getData()));
                    }
                }
            }

            if (text1.replaceAll("\t", "").isEmpty()) {
                continue;
            }
            //TODO:currently question can't start with the word Question NOR feedback. NOR \t
            if (text1.toLowerCase().strip().startsWith("question")) {

                if (!answers.isEmpty()) {
                    questions.add(new Question(questionName, defaultFeedback, questionType, question, answers, (ArrayList<String>) paragraphPictures.clone()));
                    paragraphPictures.clear();
                }

                if (text1.toLowerCase().contains("single choice")) {
                    questionType = QuestionType.SINGLE_CHOICE;
                }

                int endOfNameIndex = text1.indexOf("-") == -1 ? text1.indexOf("–") : text1.indexOf("-"); //TODO: add exception
                questionName = text1.substring(0, endOfNameIndex);
                previousQuestionType = true;
                continue;
            }
            if (previousQuestionType) {
                question = text1;
                previousQuestionType = false;
                continue;
            }
            if (text1.strip().toLowerCase().startsWith("default feedback")) {
                defaultFeedback = text1.toLowerCase().replace("default feedback:", "").strip();
            } else if (text1.toLowerCase().strip().startsWith("feedback")) {
                questionFeedbackText = text1.toLowerCase().replace("feedback", "").strip().replace(":", "").strip(); //TODO: add regex
            } else {
                answers.add(createAnswerFromString(text1, questionFeedbackText));
                questionFeedbackText = "";
            }
            previousQuestionType = false;


        }

        if (!answers.isEmpty()) {
            questions.add(new Question(questionName, defaultFeedback, questionType, question, answers, (ArrayList<String>) paragraphPictures.clone()));
            paragraphPictures.clear();
        }

        return questions;
    }

    private Answer createAnswerFromString(String text, String feedback) {
        var answerText = "";
        var isCorrectAnswer = false;

        if (text.strip().startsWith("*")) {
            answerText = text.substring(1);
            isCorrectAnswer = true;

        } else {
            answerText = text;
            isCorrectAnswer = false;
        }

        return new Answer(answerText, feedback, isCorrectAnswer);
    }
}
