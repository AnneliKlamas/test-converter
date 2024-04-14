package com.quiz.converter.services;

import com.quiz.converter.models.Answer;
import com.quiz.converter.models.Picture;
import com.quiz.converter.models.Question;
import com.quiz.converter.models.enums.QuestionType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class CourseraDocxCreatorService {
    public byte[] createCourseraDocx(List<Question> questions) throws IOException {
        var doc = new XWPFDocument();
        for (var question : questions) {
            addQuestionDetails(questions, question, doc);
            addQuestionDescription(question, doc);

            for (int i = 0; i < question.answerOptions().size(); i++) {
                addAnswerOptions(question, i, doc);
            }

            addDefaultFeedback(question, doc);
        }

        var bos = new ByteArrayOutputStream();
        doc.write(bos);
        doc.close();

        return bos.toByteArray();
    }

    private void addDefaultFeedback(Question question, XWPFDocument doc) {
        if (!question.defaultFeedback().isEmpty()) {
            var defaultFeedback = doc.createParagraph();
            var defaultFeedbackRun = defaultFeedback.createRun();
            defaultFeedbackRun.setText("Default Feedback: " + question.defaultFeedback());
        }
    }

    private static void addQuestionDescription(Question question, XWPFDocument doc) {
        for (var description : question.description().getTexts()) {
            var questionDescription = doc.createParagraph();
            var questionDescriptionRun = questionDescription.createRun();
            questionDescriptionRun.setText(description);
        }

        question.description().getPictures().forEach(p -> addPictures(p, doc));
    }

    private static void addQuestionDetails(List<Question> questions, Question question, XWPFDocument doc) {
        var questionDetails = doc.createParagraph();
        var questionDetailsRun = questionDetails.createRun();
        var questionTypeText = "";
        if (question.type().equals(QuestionType.SINGLE_CHOICE)) {
            questionTypeText = "single correct answer";
        } else if (question.type().equals(QuestionType.MULTIPLE_CHOICE)) {
            questionTypeText = "checkbox";
        } else if (question.type().equals(QuestionType.TEXT_MATCH)) {
            questionTypeText = "text match";
        }
        int questionName;
        try {
            questionName = Integer.parseInt(question.name());
        } catch (NumberFormatException e) {
            questionName = questions.indexOf(question) + 1;
        }
        var shuffle = question.shuffle() ? "shuffle" : "no shuffle";
        var partialCredit = question.partialCredit() ? "partial credit" : "no partial credit";
        questionDetailsRun.setText("Question " + questionName + " - " + questionTypeText + ", " + shuffle + ", " + partialCredit);
    }

    private static void addAnswerOptions(Question question, int i, XWPFDocument doc) {
        var answer = question.answerOptions().get(i);
        var answerParagraph = doc.createParagraph();
        var answerRun = answerParagraph.createRun();

        var text = answer.isCorrect() ? "*" : "";
        text += (char) ('A' + i) + ": " + answer.getText();
        answerRun.setText(text);

        answer.getPictures().forEach(p -> addPictures(p, doc));

        addAnswerFeedback(doc, answer);
    }

    private static void addAnswerFeedback(XWPFDocument doc, Answer answer) {
        if (answer.getFeedback() != null && answer.getFeedback().isPresent()) {
            var feedbackParagraph = doc.createParagraph();
            var feedbackRun = feedbackParagraph.createRun();
            feedbackRun.setText("Feedback: " + answer.getFeedback().get());
        }
    }

    private static void addPictures(Picture p, XWPFDocument doc) {
        var pictureParagraph = doc.createParagraph();
        var pictureRun = pictureParagraph.createRun();
        var pictureData = Base64.getDecoder().decode(p.base64());
        try {
            pictureRun.addPicture(new ByteArrayInputStream(pictureData), p.type(), p.name(), Units.toEMU(p.width()), Units.toEMU(p.height()));
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
