package com.quiz.converter.services;

import com.quiz.converter.models.Answer;
import com.quiz.converter.models.Picture;
import com.quiz.converter.models.Question;
import com.quiz.converter.models.enums.QuestionType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class MoodleXmlCreatorService {
    public byte[] createMoodleXml(List<Question> questions) throws ParserConfigurationException, TransformerException {
        var docFactory = DocumentBuilderFactory.newInstance();
        var docBuilder = docFactory.newDocumentBuilder();

        var doc = docBuilder.newDocument();
        var rootElement = doc.createElement("quiz");
        doc.appendChild(rootElement);

        questions.forEach(question -> addQuestion(question, doc, rootElement));

        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        var source = new DOMSource(doc);

        var bos = new ByteArrayOutputStream();
        var result = new StreamResult(bos);
        transformer.transform(source, result);

        return bos.toByteArray();
    }

    private static void addQuestion(Question question, Document doc, Element rootElement) {
        var questionElem = doc.createElement("question");

        setQuestionType(question, rootElement, questionElem);
        setQuestionName(question, doc, questionElem);
        addQuestionDescription(question, doc, questionElem);

        for (var answer : question.answerOptions()) {
            if (question.type().equals(QuestionType.REGULAR_EXPRESSION) && !answer.isCorrect()) continue;
            addAnswer(doc, answer, questionElem, question);
        }

        addQuestionOptions(question, doc, questionElem);
        if (question.type().equals(QuestionType.MULTIPLE_CHOICE) || question.type().equals(QuestionType.SINGLE_CHOICE)) {
            addQuestionType(question, doc, questionElem);
        }

        if (!question.defaultFeedback().isEmpty()) {
            addFeedback(doc, questionElem, question.defaultFeedback());
        }
    }

    private static void addQuestionOptions(Question question, Document doc, Element questionElem) {
        if (question.type().equals(QuestionType.REGULAR_EXPRESSION)) {
            var showAlternateElem = doc.createElement("studentshowalternate");
            showAlternateElem.setTextContent("0");
            questionElem.appendChild(showAlternateElem);
        }
        if (question.type().equals(QuestionType.MULTIPLE_CHOICE) || question.type().equals(QuestionType.SINGLE_CHOICE)) {
            var shuffleTagElem = doc.createElement("shuffleanswers");
            var shuffleText = question.shuffle() ? "1" : "0";
            shuffleTagElem.setTextContent(shuffleText);
            questionElem.appendChild(shuffleTagElem);
        }
    }

    private static void addQuestionType(Question question, Document doc, Element questionElem) {
        var singleChoiceTagElem = doc.createElement("single");
        var singleChoiceText = question.type().equals(QuestionType.SINGLE_CHOICE) ? "true" : "false";
        singleChoiceTagElem.setTextContent(singleChoiceText);
        questionElem.appendChild(singleChoiceTagElem);
    }

    private static void addQuestionDescription(Question question, Document doc, Element questionElem) {
        var questionTextElem = doc.createElement("questiontext");

        questionTextElem.setAttribute("format", "html");
        questionElem.appendChild(questionTextElem);

        var textElem = doc.createElement("text");
        var descriptionContent = new StringBuilder();
        for (var description : question.description().getTexts()) {
            descriptionContent.append("<br>").append(description);
        }

        for (var picture : question.description().getPictures()) {
            descriptionContent.append(addPicture(doc, picture, questionTextElem));
        }

        textElem.setTextContent(descriptionContent.toString());
        questionTextElem.appendChild(textElem);
    }

    private static void setQuestionName(Question question, Document doc, Element questionElem) {
        var nameElem = doc.createElement("name");
        questionElem.appendChild(nameElem);
        var textElem = doc.createElement("text");
        textElem.setTextContent(question.name());
        nameElem.appendChild(textElem);
    }

    private static void setQuestionType(Question question, Element rootElem, Element questionElem) {
        var type = getQuestionType(question.type());
        questionElem.setAttribute("type", type);
        rootElem.appendChild(questionElem);
    }

    private static String getQuestionType(QuestionType type) {
        return switch (type) {
            case SINGLE_CHOICE, MULTIPLE_CHOICE -> "multichoice";
            case TEXT_MATCH -> "shortanswer";
            case REGULAR_EXPRESSION -> "regexp";
            default -> "";
        };
    }

    private static void addAnswer(Document doc, Answer answer, Element questionElem, Question question) {
        var correctAnswerCount = (int) question.answerOptions().stream().filter(Answer::isCorrect).count();

        var answerElem = doc.createElement("answer");
        var fraction = 0.0;
        if (question.type().equals(QuestionType.MULTIPLE_CHOICE)) {
            fraction = answer.isCorrect() ? 100.0 / correctAnswerCount : -100.0 / correctAnswerCount;
        } else {
            fraction = answer.isCorrect() ? 100 : 0;
        }
        answerElem.setAttribute("fraction", String.valueOf(fraction));
        answerElem.setAttribute("format", "html");
        questionElem.appendChild(answerElem);

        var textElem = doc.createElement("text");
        var answerContent = new StringBuilder(answer.getText());

        for (var picture : answer.getPictures()) {
            answerContent.append(addPicture(doc, picture, answerElem));
        }

        textElem.setTextContent(answerContent.toString());
        answerElem.appendChild(textElem);

        if (answer.getFeedback() != null && answer.getFeedback().isPresent()) {
            var feedbackElem = doc.createElement("feedback");
            answerElem.appendChild(feedbackElem);
            var feedbackTextElem = doc.createElement("text");
            feedbackTextElem.setTextContent(answer.getFeedback().get());
            feedbackElem.appendChild(feedbackTextElem);
        }
    }

    private static void addFeedback(Document doc, Element questionElem, String feedback) {
        var generalFeedbackElem = doc.createElement("generalfeedback");
        questionElem.appendChild(generalFeedbackElem);
        var generalFeedbackTextElem = doc.createElement("text");
        generalFeedbackTextElem.setTextContent(feedback);
        generalFeedbackElem.appendChild(generalFeedbackTextElem);
    }

    private static String addPicture(Document doc, Picture picture, Element parentElem) {
        var fileElem = doc.createElement("file");
        fileElem.setAttribute("name", "moodle_" + picture.name() + picture.type().extension);
        fileElem.setAttribute("encoding", "base64");
        fileElem.setTextContent(picture.base64());
        parentElem.appendChild(fileElem);
        return "<br><img src=\"@@PLUGINFILE@@/moodle_" +
                picture.name() + picture.type().extension +
                "\" alt=\"" +
                picture.name() +
                "\" width=\"" + picture.width() + "\" height=\"" + picture.height() + "\" class=\"img-fluid atto_image_button_text-bottom\"><br>";
    }
}
