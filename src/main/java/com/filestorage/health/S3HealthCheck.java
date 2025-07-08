package com.filestorage.health;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.codahale.metrics.health.HealthCheck;
import com.filestorage.config.FileStorageConfiguration;

public class S3HealthCheck extends HealthCheck {
    private final FileStorageConfiguration.S3Config s3Config;

    public S3HealthCheck(FileStorageConfiguration.S3Config s3Config) {
        this.s3Config = s3Config;
    }

    @Override
    protected Result check() throws Exception {
        try {
            if (!s3Config.isEnabled()) {
                return Result.healthy("S3 storage is disabled");
            }
            
            BasicAWSCredentials credentials = new BasicAWSCredentials(
                s3Config.getAccessKey(), 
                s3Config.getSecretKey()
            );
            
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(s3Config.getRegion())
                    .build();

            s3Client.doesBucketExistV2(s3Config.getBucketName());

            return Result.healthy();
        } catch (Exception e) {
            return Result.unhealthy("S3 connection failed: " + e.getMessage());
        }
    }
}