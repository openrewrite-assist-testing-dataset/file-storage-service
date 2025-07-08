package com.filestorage.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.filestorage.config.FileStorageConfiguration;
import com.filestorage.model.FileMetadata;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public class FileStorageService {
    
    private final FileStorageConfiguration.S3Config s3Config;
    private final FileStorageConfiguration.LocalStorageConfig localStorageConfig;
    private final AmazonS3 s3Client;
    
    public FileStorageService(FileStorageConfiguration.S3Config s3Config, 
                             FileStorageConfiguration.LocalStorageConfig localStorageConfig) {
        this.s3Config = s3Config;
        this.localStorageConfig = localStorageConfig;
        
        if (s3Config.isEnabled()) {
            BasicAWSCredentials credentials = new BasicAWSCredentials(
                s3Config.getAccessKey(), 
                s3Config.getSecretKey()
            );
            
            AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(s3Config.getRegion());
                
            if (s3Config.getEndpoint() != null) {
                builder.withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(s3Config.getEndpoint(), s3Config.getRegion())
                );
            }
            
            this.s3Client = builder.build();
        } else {
            this.s3Client = null;
        }
    }
    
    public FileMetadata uploadFile(InputStream inputStream, String fileName, String uploadedBy) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String storedName = fileId + "_" + fileName;
        
        // Create metadata object
        FileMetadata metadata = new FileMetadata();
        metadata.setId(fileId);
        metadata.setFileName(fileName);
        metadata.setUploadedBy(uploadedBy);
        metadata.setUploadDate(new Date());
        
        // Determine storage location
        String storageLocation;
        if (s3Config.isEnabled() && s3Client != null) {
            storageLocation = storeFileInS3(inputStream, storedName, metadata);
        } else {
            storageLocation = storeFileLocally(inputStream, storedName, metadata);
        }
        
        metadata.setStoragePath(storageLocation);
        return metadata;
    }
    
    public InputStream downloadFile(String storagePath) throws IOException {
        return retrieveFile(storagePath);
    }
    
    private String storeFileInS3(InputStream inputStream, String storedName, FileMetadata metadata) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(metadata.getMimeType());
        objectMetadata.setContentLength(metadata.getFileSize());
        objectMetadata.setLastModified(new Date());
        
        if (metadata.getContentEncoding() != null) {
            objectMetadata.setContentEncoding(metadata.getContentEncoding());
        }
        
        PutObjectRequest request = new PutObjectRequest(
            s3Config.getBucketName(), 
            storedName, 
            inputStream, 
            objectMetadata
        );
        
        s3Client.putObject(request);
        
        return "s3://" + s3Config.getBucketName() + "/" + storedName;
    }
    
    private String storeFileLocally(InputStream inputStream, String storedName, FileMetadata metadata) throws IOException {
        File storageDir = new File(localStorageConfig.getBasePath());
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        
        File targetFile = new File(storageDir, storedName);
        
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            IOUtils.copy(inputStream, outputStream);
        }
        
        return "file://" + targetFile.getAbsolutePath();
    }
    
    public InputStream retrieveFile(String storageLocation) throws IOException {
        if (storageLocation.startsWith("s3://")) {
            return retrieveFileFromS3(storageLocation);
        } else if (storageLocation.startsWith("file://")) {
            return retrieveFileLocally(storageLocation);
        } else {
            throw new IllegalArgumentException("Unsupported storage location: " + storageLocation);
        }
    }
    
    private InputStream retrieveFileFromS3(String storageLocation) throws IOException {
        String key = storageLocation.substring(("s3://" + s3Config.getBucketName() + "/").length());
        S3Object object = s3Client.getObject(s3Config.getBucketName(), key);
        return object.getObjectContent();
    }
    
    private InputStream retrieveFileLocally(String storageLocation) throws IOException {
        String filePath = storageLocation.substring("file://".length());
        File file = new File(filePath);
        return FileUtils.openInputStream(file);
    }
    
    public void deleteFile(String storageLocation) throws IOException {
        if (storageLocation.startsWith("s3://")) {
            deleteFileFromS3(storageLocation);
        } else if (storageLocation.startsWith("file://")) {
            deleteFileLocally(storageLocation);
        }
    }
    
    private void deleteFileFromS3(String storageLocation) {
        String key = storageLocation.substring(("s3://" + s3Config.getBucketName() + "/").length());
        s3Client.deleteObject(s3Config.getBucketName(), key);
    }
    
    private void deleteFileLocally(String storageLocation) throws IOException {
        String filePath = storageLocation.substring("file://".length());
        File file = new File(filePath);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
    }
    
    public boolean fileExists(String storageLocation) {
        try {
            if (storageLocation.startsWith("s3://")) {
                String key = storageLocation.substring(("s3://" + s3Config.getBucketName() + "/").length());
                return s3Client.doesObjectExist(s3Config.getBucketName(), key);
            } else if (storageLocation.startsWith("file://")) {
                String filePath = storageLocation.substring("file://".length());
                return new File(filePath).exists();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}