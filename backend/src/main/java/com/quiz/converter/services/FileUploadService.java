package com.quiz.converter.services;

import com.quiz.converter.handlers.FeedbackHandler;
import com.quiz.converter.handlers.QuestionHandler;
import com.quiz.converter.models.Answer;
import com.quiz.converter.models.Question;
import com.quiz.converter.models.QuestionDescription;
import com.quiz.converter.models.QuestionState;
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

        if (!state.getAnswerOptions().isEmpty()) questions.add(state.createQuestion());

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
                state.setDescription(new QuestionDescription(text, paragraphPictures));
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

    private static void handleEmptyTextParagraph(List<String> paragraphPictures, QuestionState state) {
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
        if (lowerCaseText.startsWith("question")) return ParagraphType.QUESTION_DETAILS;
        if (state.getPreviousParagraphType().equals(ParagraphType.QUESTION_DETAILS))
            return ParagraphType.QUESTION_DESCRIPTION;
        if (lowerCaseText.startsWith("feedback")) return ParagraphType.FEEDBACK;
        if (lowerCaseText.startsWith("default feedback")) return ParagraphType.DEFAULT_FEEDBACK;
        return ParagraphType.ANSWER_OPTION;
    }

    private List<String> handleParagraphPictures(XWPFParagraph paragraph) {
        var runsWithPictures = paragraph.getRuns().stream().filter(r -> !r.getEmbeddedPictures().isEmpty()).toList();
        var paragraphPictures = new ArrayList<String>();
        if (!runsWithPictures.isEmpty()) {
            for (var run : runsWithPictures) {
                var pictures = run.getEmbeddedPictures();
                for (var picture : pictures) {
                    paragraphPictures.add(Base64.getEncoder().encodeToString(picture.getPictureData().getData()));
                }
            }
        }
        return paragraphPictures;
    }

    private Answer createAnswerFromString(String text, List<String> paragraphPictures) {
        var isCorrectAnswer = text.strip().startsWith("*");
        var indexOfColon = !text.contains(":") ? Integer.MAX_VALUE : text.indexOf(":");
        var indexOfParenthesis = !text.contains(")") ? Integer.MAX_VALUE : text.indexOf(")");

        if (text.contains(")") || text.contains(":")) {
            text = text.substring(Math.min(indexOfColon, indexOfParenthesis) + 1);
        }

        return new Answer(text, null, isCorrectAnswer, paragraphPictures);
    }
}
