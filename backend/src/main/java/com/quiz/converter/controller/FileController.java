package com.quiz.converter.controller;

import com.quiz.converter.services.ConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

@Controller
@RequestMapping(value = "/file/convert")
public class FileController {

    @Autowired
    ConverterService service;

    @PostMapping(value = "/moodleXML")
    public ResponseEntity<MultiValueMap<String, HttpEntity<?>>> convertDocFileToMoodleXML(@RequestBody MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        var convertedFileName = file.getOriginalFilename().replace(".docx", "") + "_moodle.xml";
        var attachments = service.convertDocToMoodle(file);

        var builder = new MultipartBodyBuilder();
        builder.part("details", attachments.getSecond())
                .contentType(MediaType.APPLICATION_JSON);
        builder.part("file", attachments.getFirst())
                .contentType(MediaType.APPLICATION_XML)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + convertedFileName);
        var body = builder.build();

        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body);
    }

    @PostMapping(value = "/courseraDocx")
    public ResponseEntity<byte[]> convertDocFileToCourseraDocx(@RequestBody MultipartFile file) throws IOException {
        var convertedFileName = file.getOriginalFilename().replace(".docx", "") + "_coursera.docx";
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.ms-word"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + convertedFileName)
                .body(service.convertDocToCoursera(file));
    }
}
