package com.example.pro.files;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TestFileUploader extends FileUploader{
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private static final String TEST_KEY = "test/";

    @Override
    public String uploadFile(MultipartFile multipartFile, String path) {
        ObjectMetadata objectMetaData = createObjectMetaData(multipartFile);
        String originalFilename = multipartFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String key = TEST_KEY + UUID.randomUUID() + "." + ext;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetaData)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException ex) {
            log.error("이미지 업로드 IOException");
            throw new S3IOException(ex.getMessage());
        }

        return amazonS3Client.getUrl(bucket, key).toString();
    }

    @Override
    public void deleteFile(String url) {
        try {
            amazonS3Client.deleteObject(bucket, url);
            log.debug("S3 이미지 삭제 성공");
        } catch(SdkClientException e) {
            throw new S3IOException(e.getMessage());
        }
    }
}
