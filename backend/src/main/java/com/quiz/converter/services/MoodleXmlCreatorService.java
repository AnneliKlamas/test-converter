package com.quiz.converter.services;

import com.quiz.converter.models.Answer;
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

        var questionNr = 1;
        for (var question : questions) {
            addQuestion(question, doc, rootElement, questionNr);
            questionNr++;
        }

        var transformerFactory = TransformerFactory.newInstance();
        var transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        var source = new DOMSource(doc);

        var bos = new ByteArrayOutputStream();
        var result = new StreamResult(bos);
        transformer.transform(source, result);

        return bos.toByteArray();

    }

    private static void addQuestion(Question question, Document doc, Element rootElement, int pictureNumber) {
        var questionElem = doc.createElement("question");

        setQuestionType(question, rootElement, questionElem);
        setQuestionName(question, doc, questionElem);
        setQuestionDescription(question, doc, pictureNumber, questionElem);

        for (var answer : question.answerOptions()) {
            addAnswer(doc, answer, questionElem);
        }

        if (question.type().equals(QuestionType.SINGLE_CHOICE)) {
            var singleChoiceTagElem = doc.createElement("single");
            singleChoiceTagElem.setTextContent("true");
            questionElem.appendChild(singleChoiceTagElem);
        }

        if (!question.feedback().isEmpty()) {
            addFeedback(doc, questionElem, question.feedback());
        }
    }

    private static void setQuestionDescription(Question question, Document doc, int pictureNumber, Element questionElem) {
        var questionText = doc.createElement("questiontext");

        questionText.setAttribute("format", "html");
        questionElem.appendChild(questionText);

        var text = doc.createElement("text");
        var paragraphContent = new StringBuilder(question.description().getText());

        for (var picture : question.description().getPictures()) {
            paragraphContent.append(addPicture(doc, pictureNumber, picture, questionText));
        }

        text.setTextContent(paragraphContent.toString());
        questionText.appendChild(text);
    }

    private static void setQuestionName(Question question, Document doc, Element questionElem) {
        var nameElem = doc.createElement("name");
        questionElem.appendChild(nameElem);
        var textElem = doc.createElement("text");
        textElem.setTextContent(question.name());
        nameElem.appendChild(textElem);
    }

    private static void setQuestionType(Question question, Element rootElem, Element questionElem) {
        var type = "";
        if (question.type().equals(QuestionType.SINGLE_CHOICE)) type = "multichoice";
        questionElem.setAttribute("type", type);
        rootElem.appendChild(questionElem);
    }

    private static void addAnswer(Document doc, Answer answer, Element questionElem) {
        var answerElem = doc.createElement("answer");
        answerElem.setAttribute("fraction", answer.isCorrect() ? "100" : "0");
        questionElem.appendChild(answerElem);

        var textElem = doc.createElement("text");
        textElem.setTextContent(answer.getText());
        answerElem.appendChild(textElem);

        if (answer.getFeedback() != null && answer.getFeedback().isPresent()) {
            var feedbackElem = doc.createElement("feedback");
            questionElem.appendChild(feedbackElem);
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

    private static String addPicture(Document doc, int pictureNumber, String picture, Element questionText) {
        var paragraphPictureNr = 0;
        var fileElem = doc.createElement("file");
        fileElem.setAttribute("name", "moodle_" + pictureNumber + paragraphPictureNr + ".png");
        fileElem.setAttribute("encoding", "base64");
        fileElem.setTextContent(picture);
        questionText.appendChild(fileElem);
        return "<br><img src=\"@@PLUGINFILE@@/moodle_" +
                pictureNumber + paragraphPictureNr +
                ".png\" alt=\"" +
                pictureNumber + paragraphPictureNr +
                "\" width=\"225\" height=\"225\" class=\"img-fluid atto_image_button_text-bottom\"><br>";
    }
}
