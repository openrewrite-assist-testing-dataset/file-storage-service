package com.filestorage.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class FileMetadata {
    
    @JsonProperty
    private String id;
    
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 255)
    private String originalName;
    
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 255)
    private String storedName;
    
    @JsonProperty
    @NotNull
    private String mimeType;
    
    @JsonProperty
    @NotNull
    private Long fileSize;
    
    @JsonProperty
    private String checksum;
    
    @JsonProperty
    private String storageLocation;
    
    @JsonProperty
    private String bucketName;
    
    @JsonProperty
    private String uploadedBy;
    
    @JsonProperty
    private Date uploadDate;
    
    @JsonProperty
    private Date lastAccessed;
    
    @JsonProperty
    private String description;
    
    @JsonProperty
    private String tags;
    
    @JsonProperty
    private boolean isPublic;
    
    @JsonProperty
    private String contentEncoding;
    
    @JsonProperty
    private String contentLanguage;
    
    @JsonProperty
    private Date expirationDate;
    
    @JsonProperty
    private String version;

    public FileMetadata() {
        this.uploadDate = new Date();
        this.lastAccessed = new Date();
        this.isPublic = false;
        this.version = "1.0";
    }

    public FileMetadata(String originalName, String storedName, String mimeType, Long fileSize, String uploadedBy) {
        this();
        this.originalName = originalName;
        this.storedName = storedName;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getOriginalName() { return originalName; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    
    public String getStoredName() { return storedName; }
    public void setStoredName(String storedName) { this.storedName = storedName; }
    
    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }
    
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
    
    public String getStoragePath() { return storageLocation; }
    public void setStoragePath(String storagePath) { this.storageLocation = storagePath; }
    
    public String getFileName() { return originalName; }
    public void setFileName(String fileName) { this.originalName = fileName; }
    
    public String getContentType() { return mimeType; }
    public void setContentType(String contentType) { this.mimeType = contentType; }
    
    public String getBucketName() { return bucketName; }
    public void setBucketName(String bucketName) { this.bucketName = bucketName; }
    
    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }
    
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
    
    public Date getLastAccessed() { return lastAccessed; }
    public void setLastAccessed(Date lastAccessed) { this.lastAccessed = lastAccessed; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    
    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    
    public String getContentEncoding() { return contentEncoding; }
    public void setContentEncoding(String contentEncoding) { this.contentEncoding = contentEncoding; }
    
    public String getContentLanguage() { return contentLanguage; }
    public void setContentLanguage(String contentLanguage) { this.contentLanguage = contentLanguage; }
    
    public Date getExpirationDate() { return expirationDate; }
    public void setExpirationDate(Date expirationDate) { this.expirationDate = expirationDate; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public void updateLastAccessed() {
        this.lastAccessed = new Date();
    }
    
    public boolean isExpired() {
        return expirationDate != null && new Date().after(expirationDate);
    }
}