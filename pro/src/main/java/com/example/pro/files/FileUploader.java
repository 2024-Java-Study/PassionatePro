package com.example.pro.files;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String uploadFile(MultipartFile multipartFile, String path);
}
