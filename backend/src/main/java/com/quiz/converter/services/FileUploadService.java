package com.quiz.converter.services;

import com.quiz.converter.handlers.FeedbackHandler;
import com.quiz.converter.handlers.QuestionHandler;
import com.quiz.converter.handlers.QuestionValidationHandler;
import com.quiz.converter.models.*;
import com.quiz.converter.models.enums.ParagraphType;
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

        var state = new QuestionState();
        var questions = new ArrayList<Question>();

        docx.getParagraphs().forEach(paragraph -> handleParagraph(state, questions, paragraph));

        var validationHandler = new QuestionValidationHandler(state);
        validationHandler.validateQuestion();
        questions.add(state.createQuestion());

        return questions;
    }

    private void handleParagraph(QuestionState state, ArrayList<Question> questions, XWPFParagraph paragraph) {
        var text = paragraph.getParagraphText();
        var paragraphPictures = handleParagraphPictures(paragraph);
        var paragraphType = getParagraphType(text, state);

        switch (paragraphType) {
            case EMPTY_TEXT -> handleEmptyTextParagraph(paragraphPictures, state);
            case QUESTION_DETAILS -> {
                var questionHandler = new QuestionHandler(state);
                questionHandler.handleQuestion(text).ifPresent(questions::add);
                state.setPreviousParagraphType(paragraphType);
            }
            case QUESTION_DESCRIPTION -> {
                var descriptionTexts = state.getDescription().getTexts();
                descriptionTexts.add(text);
                var descriptionPictures = state.getDescription().getPictures();
                descriptionPictures.addAll(paragraphPictures);
                state.setPreviousParagraphType(paragraphType);
            }
            case FEEDBACK, DEFAULT_FEEDBACK -> {
                var feedbackHandler = new FeedbackHandler(state);
                feedbackHandler.add(text, paragraphType);
                state.setPreviousParagraphType(paragraphType);
            }
            case ANSWER_OPTION -> {
                state.getAnswerOptions().add(createAnswerFromString(text, paragraphPictures));
                state.setPreviousParagraphType(paragraphType);
            }
        }
    }

    private static void handleEmptyTextParagraph(List<Picture> paragraphPictures, QuestionState state) {
        if (!paragraphPictures.isEmpty()) {
            if (state.getPreviousParagraphType().equals(ParagraphType.ANSWER_OPTION)) {
                state.getAnswerOptions().get(state.getAnswerOptions().size() - 1).getPictures().addAll(paragraphPictures);
            } else if (state.getPreviousParagraphType().equals(ParagraphType.QUESTION_DESCRIPTION)) {
                state.getDescription().getPictures().addAll(paragraphPictures);
            }
        }
    }

    private ParagraphType getParagraphType(String text, QuestionState state) {
        if (text.replaceAll("\t", "").isEmpty() || text.replaceAll("\n", "").isEmpty()) return ParagraphType.EMPTY_TEXT;
        var lowerCaseText = text.toLowerCase().strip();
        if (lowerCaseText.matches("\s*(question).*")) return ParagraphType.QUESTION_DETAILS;
        if (lowerCaseText.matches("\s*(feedback).*")) return ParagraphType.FEEDBACK;
        if (lowerCaseText.matches(".*(default)\s*(feedback).*")) return ParagraphType.DEFAULT_FEEDBACK;
        if (lowerCaseText.matches("(\\s*\\**\\s*)*[a-zA-Z\\d]\\s*[:)].*")) return ParagraphType.ANSWER_OPTION;
        if (state.getPreviousParagraphType().equals(ParagraphType.QUESTION_DETAILS))
            return ParagraphType.QUESTION_DESCRIPTION;
        if (state.getPreviousParagraphType().equals(ParagraphType.QUESTION_DESCRIPTION)) {
            return ParagraphType.QUESTION_DESCRIPTION;
        }
        return ParagraphType.UNKNOWN;
    }

    private List<Picture> handleParagraphPictures(XWPFParagraph paragraph) {
        var runsWithPictures = paragraph.getRuns().stream().filter(r -> !r.getEmbeddedPictures().isEmpty()).toList();
        var paragraphPictures = new ArrayList<Picture>();
        if (!runsWithPictures.isEmpty()) {
            for (var run : runsWithPictures) {
                var pictures = run.getEmbeddedPictures();
                for (var picture : pictures) {
                    var data = Base64.getEncoder().encodeToString(picture.getPictureData().getData());
                    var name = picture.getPictureData().getFileName().replace(picture.getPictureData().getPictureTypeEnum().extension, "");
                    paragraphPictures.add(new Picture(data, picture.getWidth(), picture.getDepth(), name, picture.getPictureData().getPictureTypeEnum()));
                }
            }
        }
        return paragraphPictures;
    }

    private Answer createAnswerFromString(String text, List<Picture> paragraphPictures) {
        var isCorrectAnswer = text.strip().matches("(\\s*\\*+\s*).*");
        var answerText = text.strip().replaceAll("(\\s*\\**\\s*)*[a-zA-Z\\d]\\s*[:)]", "");
        return new Answer(answerText, null, isCorrectAnswer, paragraphPictures);
    }
}
