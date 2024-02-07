package com.example.pro.files;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUploader implements FileUploader{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile multipartFile, String path) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/png");
        objectMetadata.setContentLength(multipartFile.getSize());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, path, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ex) {
            log.error("이미지 업로드 IOException");
            throw new S3IOException(ex.getMessage());
        }

        return amazonS3Client.getUrl(bucket, path).toString();
    }
}
