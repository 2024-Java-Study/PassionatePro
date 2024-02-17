package com.example.pro.files;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileUploader {

    public abstract String uploadFile(MultipartFile multipartFile, String path);

    public ObjectMetadata createObjectMetaData(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }
}
