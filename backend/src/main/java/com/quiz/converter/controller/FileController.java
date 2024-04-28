package com.quiz.converter.controller;

import com.quiz.converter.models.FileDto;
import com.quiz.converter.models.QuizDetails;
import com.quiz.converter.services.ConverterService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping(value = "/file/convert")
public class FileController {

    @Autowired
    ConverterService service;

    @PostMapping("/moodleXML")
    public ResponseEntity<FileDto> convertDocFileToMoodleXML(@RequestBody MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        var conversionResult = service.convertDocToMoodle(file);
        var fileContent = conversionResult.getFirst();
        var details = conversionResult.getSecond();
        var base64EncodedFile = Base64.encodeBase64String(fileContent);

        FileDto response = new FileDto(Objects.requireNonNull(file.getOriginalFilename()).replace(".docx", "") + "_moodle.xml", base64EncodedFile, details);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/courseraDocx")
    public ResponseEntity<FileDto> convertDocFileToCourseraDocx(@RequestBody MultipartFile file) throws IOException {
        var conversionResult = service.convertDocToCoursera(file);
        var fileContent = conversionResult.getFirst();
        var details = conversionResult.getSecond();
        var base64EncodedFile = Base64.encodeBase64String(fileContent);

        FileDto response = new FileDto(Objects.requireNonNull(file.getOriginalFilename()).replace(".docx", "") + "_coursera.docx", base64EncodedFile, details);

        return ResponseEntity.ok(response);
    }
}