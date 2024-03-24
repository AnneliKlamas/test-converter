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

        for (XWPFParagraph paragraph : docx.getParagraphs()) {
            var text = paragraph.getParagraphText();
            var paragraphPictures = handleParagraphPictures(paragraph);
            var paragraphType = getParagraphType(text, state);

            if (paragraphType == ParagraphType.EMPTY_TEXT) {
                handleEmptyTextParagraph(paragraphPictures, state);
                continue;
            }

            if (paragraphType.equals(ParagraphType.QUESTION_DETAILS)) {
                var questionHandler = new QuestionHandler(state);
                state.getDescriptionPictures().addAll(paragraphPictures);
                questionHandler.handleQuestion(text).ifPresent(questions::add);
                state.setPreviousParagraphType(paragraphType);
                continue;
            }

            if (paragraphType == ParagraphType.QUESTION_DESCRIPTION) {
                state.setDescription(new QuestionDescription(text, paragraphPictures));
                continue;
            }
            if (paragraphType == ParagraphType.FEEDBACK || paragraphType == ParagraphType.DEFAULT_FEEDBACK) {
                var feedbackHandler = new FeedbackHandler(state);
                feedbackHandler.add(text, paragraphType);
                continue;
            }

            if (paragraphType == ParagraphType.ANSWER_OPTION) {
                state.getAnswerOptions().add(createAnswerFromString(text, state, paragraphPictures));
                state.cleanState();
            }
        }

        if (!state.getAnswerOptions().isEmpty()) {
            questions.add(state.createQuestion());
        }

        return questions;
    }

    private static void handleEmptyTextParagraph(List<String> paragraphPictures, QuestionState state) {
        if (!paragraphPictures.isEmpty()) {
            if (state.getPreviousParagraphType() == ParagraphType.ANSWER_OPTION) {
                state.getAnswerOptions().get(state.getAnswerOptions().size() - 1).pictures().addAll(paragraphPictures);
            } else if (state.getPreviousParagraphType() == ParagraphType.QUESTION_DESCRIPTION) {
                state.getDescription().getPictures().addAll(paragraphPictures);
            }
        }
    }

    private ParagraphType getParagraphType(String text, QuestionState state) {
        if (text.replaceAll("\t", "").isEmpty()) return ParagraphType.EMPTY_TEXT;
        var lowerCaseText = text.toLowerCase().strip();
        if (lowerCaseText.startsWith("description")) return ParagraphType.QUESTION_DETAILS;
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

    private Answer createAnswerFromString(String text, QuestionState state, List<String> paragraphPictures) {
        var isCorrectAnswer = text.strip().startsWith("*");

        var indexOfColon = text.indexOf(":");
        var indexOfParenthesis = text.indexOf(")");

        if (indexOfColon != -1 && indexOfParenthesis != -1) {
            if (indexOfColon < indexOfParenthesis) {
                text = text.substring(indexOfColon + 1);
            } else {
                text = text.substring(indexOfParenthesis + 1);
            }
        }

        return new Answer(text, state.getQuestionFeedbackText(), isCorrectAnswer, paragraphPictures);
    }
}
