package com.quiz.converter.services;

import com.quiz.converter.models.Answer;
import com.quiz.converter.models.Question;
import com.quiz.converter.models.enums.QuestionType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
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
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("quiz");
        doc.appendChild(rootElement);

        var questionNr = 1;
        for (var question : questions) {
            addQuestion(question, doc, rootElement, questionNr);
            questionNr++;
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(bos);
        transformer.transform(source, result);

        return bos.toByteArray();

    }

    private static void addQuestion(Question question, Document doc, Element rootElement, int pictureNumber) {
        Element questionElem = doc.createElement("description");

        setQuestionType(question, rootElement, questionElem);
        setQuestionName(question, doc, questionElem);
        setQuestionDescription(question, doc, pictureNumber, questionElem);

        for (var answer : question.answerOptions()) {
            addAnswer(doc, answer, questionElem);
        }

        if (question.type() == QuestionType.SINGLE_CHOICE) {
            Element singleChoiceTagElem = doc.createElement("single");
            singleChoiceTagElem.setTextContent("true");
            questionElem.appendChild(singleChoiceTagElem);
        }

        if (!question.feedback().isEmpty()) {
            addFeedback(doc, questionElem, question.feedback());
        }
    }

    private static void setQuestionDescription(Question question, Document doc, int pictureNumber, Element questionElem) {
        Element questionText = doc.createElement("questiontext");
        questionText.setAttribute("format", "html");

        questionElem.appendChild(questionText);

        Element text = doc.createElement("text");
        var paragraphContent = question.description().getText();

        for (var picture : question.description().getPictures()) {
            paragraphContent += addPicture(doc, pictureNumber, picture, questionText);
        }

        text.setTextContent(paragraphContent);
        questionText.appendChild(text);
    }

    private static void setQuestionName(Question question, Document doc, Element questionElem) {
        Element name = doc.createElement("name");
        questionElem.appendChild(name);
        Element nameText = doc.createElement("text");
        nameText.setTextContent(question.name());
        name.appendChild(nameText);
    }

    private static void setQuestionType(Question question, Element rootElement, Element questionElem) {
        var type = "";
        if (question.type() == QuestionType.SINGLE_CHOICE) type = "multichoice";
        questionElem.setAttribute("type", type);
        rootElement.appendChild(questionElem);
    }

    private static void addAnswer(Document doc, Answer answer, Element questionElem) {
        Element answerElement = doc.createElement("answer");
        answerElement.setAttribute("fraction", answer.isCorrect() ? "100" : "0");
        questionElem.appendChild(answerElement);

        Element answerText = doc.createElement("text");
        answerText.setTextContent(answer.text());
        answerElement.appendChild(answerText);

        if (!answer.feedback().isEmpty()) {
            Element feedbackElement = doc.createElement("feedback");
            questionElem.appendChild(feedbackElement);
            Element feedbackText = doc.createElement("text");
            feedbackText.setTextContent(answer.feedback());
            feedbackElement.appendChild(feedbackText);
        }
    }

    private static void addFeedback(Document doc, Element questionElem, String feedback) {
        Element generalFeedbackElement = doc.createElement("generalfeedback");
        questionElem.appendChild(generalFeedbackElement);
        Element generalFeedbackTextElement = doc.createElement("text");
        generalFeedbackTextElement.setTextContent(feedback);
        generalFeedbackElement.appendChild(generalFeedbackTextElement);
    }

    private static String addPicture(Document doc, int pictureNumber, String picture, Element questionText) {
        var paragraphPictureNr = 0;
        Element fileElem = doc.createElement("file");
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
