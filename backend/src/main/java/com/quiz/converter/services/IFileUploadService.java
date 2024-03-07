package com.quiz.converter.services;

import com.quiz.converter.models.Question;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface IFileUploadService {
    Question convertDocToQuestion(MultipartFile file) throws IOException;
}

