package com.quiz.converter.services;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class MoodleXmlCreatorService {
    public byte[] createMoodleXml(List<Question> questions) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        var pictureNumber = 0;

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("quiz");
        doc.appendChild(rootElement);

        for (var question : questions) {
            Element questionElem = doc.createElement("question");

            var type = "";
            if (question.type() == QuestionType.SINGLE_CHOICE) type = "multichoice";
            questionElem.setAttribute("type", type);
            rootElement.appendChild(questionElem);

            Element name = doc.createElement("name");
            questionElem.appendChild(name);
            Element nameText = doc.createElement("text");
            nameText.setTextContent(question.name());
            name.appendChild(nameText);

            Element questionText = doc.createElement("questiontext"); //TODO: bold should be also bold on import
            questionText.setAttribute("format", "html");

            questionElem.appendChild(questionText);

            Element text = doc.createElement("text"); //TODO: remove hardcoded question text
            //text.setTextContent(question.question());
            text.setTextContent(question.question() + "<br><img src=\"@@PLUGINFILE@@/moodle_" + pictureNumber + ".png\" alt=\"kass\" width=\"225\" height=\"225\" class=\"img-fluid atto_image_button_text-bottom\"><br>");

            questionText.appendChild(text);

            for (var picture : question.pictures()) { //TODO: can image be a link?

                Element fileElem = doc.createElement("file");
                fileElem.setAttribute("name", "moodle_" + pictureNumber + ".png");
                fileElem.setAttribute("encoding", "base64"); //TODO: what happens if there are multiple pictures?
                fileElem.setTextContent(picture); //TODO: Check url things
                questionText.appendChild(fileElem);
            }

            for (var answer : question.answerOptions()) {
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

            if (question.type() == QuestionType.SINGLE_CHOICE) {
                Element singleChoiceTagElem = doc.createElement("single");
                singleChoiceTagElem.setTextContent("true");
                questionElem.appendChild(singleChoiceTagElem);
            }

            if (!question.feedback().isEmpty()) {
                Element generalFeedbackElement = doc.createElement("generalfeedback");
                questionElem.appendChild(generalFeedbackElement);
                Element generalFeedbackTextElement = doc.createElement("text");
                generalFeedbackTextElement.setTextContent(question.feedback());
                generalFeedbackElement.appendChild(generalFeedbackTextElement);
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        StreamResult result1 = new StreamResult(bos);
        transformer.transform(source, result1);
        byte[] array = bos.toByteArray();

        return array;


        // write dom document to a file
        /*try (FileOutputStream output =
                     new FileOutputStream("C:\\Users\\Anneli\\Desktop\\thesis\\test-converter\\backend\\src\\main\\resources\\static\\test_moodle.xml")) {
            writeXml(doc, output);
        } catch (IOException | TransformerException e) {
            e.printStackTrace();
        }*/

    }

    // write doc to output stream
    private static void writeXml(Document doc,
                                 FileOutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}
