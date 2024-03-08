package com.example.pro.files;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileUploader {

    String uploadFile(MultipartFile multipartFile, String path);
    void deleteFile(String url);

    default ObjectMetadata createObjectMetaData(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());
        return objectMetadata;
    }

    default String generateKey(MultipartFile file, String path) {
        if (file.getOriginalFilename() == null)
            throw new S3IOException("파일 이름이 null입니다.");
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return path + UUID.randomUUID() + "." + ext;
    }
}
