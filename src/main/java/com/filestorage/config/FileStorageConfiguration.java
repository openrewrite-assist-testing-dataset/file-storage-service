package com.filestorage.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class FileStorageConfiguration extends Configuration {
    
    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
    
    @JsonProperty("s3")
    private S3Config s3Config = new S3Config();
    
    @JsonProperty("localStorage")
    private LocalStorageConfig localStorageConfig = new LocalStorageConfig();
    
    @JsonProperty("jwtSecret")
    private String jwtSecret = "storage-secret-key-2021";
    
    @JsonProperty("apiKeys")
    private List<String> apiKeys = List.of("storage-api-key-001", "storage-api-key-002");
    
    @JsonProperty("fileUploadLimits")
    private FileUploadLimits fileUploadLimits = new FileUploadLimits();

    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    public S3Config getS3Config() {
        return s3Config;
    }

    public void setS3Config(S3Config s3Config) {
        this.s3Config = s3Config;
    }

    public LocalStorageConfig getLocalStorageConfig() {
        return localStorageConfig;
    }

    public void setLocalStorageConfig(LocalStorageConfig localStorageConfig) {
        this.localStorageConfig = localStorageConfig;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public List<String> getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(List<String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    public FileUploadLimits getFileUploadLimits() {
        return fileUploadLimits;
    }

    public void setFileUploadLimits(FileUploadLimits fileUploadLimits) {
        this.fileUploadLimits = fileUploadLimits;
    }

    public static class S3Config {
        @JsonProperty("accessKey")
        private String accessKey = "test-access-key";
        
        @JsonProperty("secretKey")
        private String secretKey = "test-secret-key";
        
        @JsonProperty("bucketName")
        private String bucketName = "file-storage-bucket";
        
        @JsonProperty("region")
        private String region = "us-east-1";
        
        @JsonProperty("endpoint")
        private String endpoint = null;
        
        @JsonProperty("enabled")
        private boolean enabled = true;

        public String getAccessKey() { return accessKey; }
        public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
        
        public String getSecretKey() { return secretKey; }
        public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
        
        public String getBucketName() { return bucketName; }
        public void setBucketName(String bucketName) { this.bucketName = bucketName; }
        
        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }
        
        public String getEndpoint() { return endpoint; }
        public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class LocalStorageConfig {
        @JsonProperty("basePath")
        private String basePath = "/tmp/file-storage";
        
        @JsonProperty("enabled")
        private boolean enabled = true;

        public String getBasePath() { return basePath; }
        public void setBasePath(String basePath) { this.basePath = basePath; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class FileUploadLimits {
        @JsonProperty("maxFileSize")
        private long maxFileSize = 100 * 1024 * 1024; // 100MB
        
        @JsonProperty("maxTotalSize")
        private long maxTotalSize = 1024 * 1024 * 1024; // 1GB
        
        @JsonProperty("allowedMimeTypes")
        private List<String> allowedMimeTypes = List.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "text/plain", "application/json",
            "application/xml", "application/zip", "application/octet-stream"
        );

        public long getMaxFileSize() { return maxFileSize; }
        public void setMaxFileSize(long maxFileSize) { this.maxFileSize = maxFileSize; }
        
        public long getMaxTotalSize() { return maxTotalSize; }
        public void setMaxTotalSize(long maxTotalSize) { this.maxTotalSize = maxTotalSize; }
        
        public List<String> getAllowedMimeTypes() { return allowedMimeTypes; }
        public void setAllowedMimeTypes(List<String> allowedMimeTypes) { this.allowedMimeTypes = allowedMimeTypes; }
    }
}