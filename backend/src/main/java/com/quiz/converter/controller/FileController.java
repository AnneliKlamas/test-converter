package com.quiz.converter.controller;

import com.quiz.converter.models.QuizDetails;
import com.quiz.converter.services.ConverterService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/file/convert")
public class FileController {

    @Autowired
    ConverterService service;

    @PostMapping("/moodleXML")
    public ResponseEntity<Map<String, Object>> convertDocFileToMoodleXML(@RequestBody MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        Pair<byte[], QuizDetails> conversionResult = service.convertDocToMoodle(file);
        byte[] fileContent = conversionResult.getFirst();
        QuizDetails details = conversionResult.getSecond();

        String base64EncodedFile = Base64.encodeBase64String(fileContent);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("file", base64EncodedFile);
        responseBody.put("fileName", file.getOriginalFilename().replace(".docx", "") + "_moodle.xml");
        responseBody.put("details", details);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/courseraDocx")
    public ResponseEntity<Map<String, Object>> convertDocFileToCourseraDocx(@RequestBody MultipartFile file) throws IOException {
        Pair<byte[], QuizDetails> conversionResult = service.convertDocToCoursera(file);
        byte[] fileContent = conversionResult.getFirst();
        QuizDetails details = conversionResult.getSecond();

        String base64EncodedFile = Base64.encodeBase64String(fileContent);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("file", base64EncodedFile);
        responseBody.put("fileName", file.getOriginalFilename().replace(".docx", "") + "_coursera.docx");
        responseBody.put("details", details);

        return ResponseEntity.ok(responseBody);
    }
}