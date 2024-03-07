package com.quiz.converter.controller;

import com.quiz.converter.models.Question;
import com.quiz.converter.services.ConverterService;
import com.quiz.converter.services.FileUploadService;
import com.quiz.converter.services.IFileUploadService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/file/convert")
public class FileController {

    @Autowired
    ConverterService service;

    @PostMapping
    public ResponseEntity<byte[]> convertDocFile(@RequestBody MultipartFile file) throws IOException, ParserConfigurationException, TransformerException {
        //TODO: filename as variable
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=moodle.xml").body(service.convertDocToMoodle(file));
    }


}














