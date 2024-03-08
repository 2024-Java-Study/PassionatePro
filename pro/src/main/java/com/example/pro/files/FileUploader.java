package com.example.pro.files;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {

    String uploadFile(MultipartFile multipartFile, String path);
    void deleteFile(String url);

    default ObjectMetadata createObjectMetaData(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }
}
