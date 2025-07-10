package com.filestorage.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FileMetadata Tests")
public class FileMetadataTest {
    
    private FileMetadata fileMetadata;
    
    @BeforeEach
    void setUp() {
        fileMetadata = new FileMetadata();
    }
    
    @Test
    @DisplayName("Should create FileMetadata with default values")
    void testDefaultConstructor() {
        assertNotNull(fileMetadata.getUploadDate());
        assertNotNull(fileMetadata.getLastAccessed());
        assertFalse(fileMetadata.isPublic());
        assertEquals("1.0", fileMetadata.getVersion());
    }
    
    @Test
    @DisplayName("Should create FileMetadata with provided values")
    void testParameterizedConstructor() {
        String originalName = "test.txt";
        String storedName = "stored_test.txt";
        String mimeType = "text/plain";
        Long fileSize = 1024L;
        String uploadedBy = "user123";
        
        FileMetadata metadata = new FileMetadata(originalName, storedName, mimeType, fileSize, uploadedBy);
        
        assertEquals(originalName, metadata.getOriginalName());
        assertEquals(storedName, metadata.getStoredName());
        assertEquals(mimeType, metadata.getMimeType());
        assertEquals(fileSize, metadata.getFileSize());
        assertEquals(uploadedBy, metadata.getUploadedBy());
        assertNotNull(metadata.getUploadDate());
        assertNotNull(metadata.getLastAccessed());
        assertFalse(metadata.isPublic());
        assertEquals("1.0", metadata.getVersion());
    }
    
    @Test
    @DisplayName("Should update last accessed date")
    void testUpdateLastAccessed() {
        Date originalDate = fileMetadata.getLastAccessed();
        
        // Wait a moment to ensure time difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        fileMetadata.updateLastAccessed();
        Date updatedDate = fileMetadata.getLastAccessed();
        
        assertTrue(updatedDate.after(originalDate));
    }
    
    @Test
    @DisplayName("Should return false for non-expired file")
    void testIsNotExpired() {
        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // 24 hours from now
        fileMetadata.setExpirationDate(futureDate);
        
        assertFalse(fileMetadata.isExpired());
    }
    
    @Test
    @DisplayName("Should return true for expired file")
    void testIsExpired() {
        Date pastDate = new Date(System.currentTimeMillis() - 86400000); // 24 hours ago
        fileMetadata.setExpirationDate(pastDate);
        
        assertTrue(fileMetadata.isExpired());
    }
    
    @Test
    @DisplayName("Should return false when expiration date is null")
    void testIsNotExpiredWhenExpirationDateIsNull() {
        fileMetadata.setExpirationDate(null);
        
        assertFalse(fileMetadata.isExpired());
    }
    
    @Test
    @DisplayName("Should set and get all properties correctly")
    void testGettersAndSetters() {
        String testId = "test-id";
        String testOriginalName = "original.txt";
        String testStoredName = "stored.txt";
        String testMimeType = "text/plain";
        Long testFileSize = 2048L;
        String testChecksum = "abc123";
        String testStorageLocation = "/storage/path";
        String testBucketName = "test-bucket";
        String testUploadedBy = "testuser";
        Date testUploadDate = new Date();
        Date testLastAccessed = new Date();
        String testDescription = "Test description";
        String testTags = "test,file";
        boolean testIsPublic = true;
        String testContentEncoding = "utf-8";
        String testContentLanguage = "en";
        Date testExpirationDate = new Date();
        String testVersion = "2.0";
        
        fileMetadata.setId(testId);
        fileMetadata.setOriginalName(testOriginalName);
        fileMetadata.setStoredName(testStoredName);
        fileMetadata.setMimeType(testMimeType);
        fileMetadata.setFileSize(testFileSize);
        fileMetadata.setChecksum(testChecksum);
        fileMetadata.setStorageLocation(testStorageLocation);
        fileMetadata.setBucketName(testBucketName);
        fileMetadata.setUploadedBy(testUploadedBy);
        fileMetadata.setUploadDate(testUploadDate);
        fileMetadata.setLastAccessed(testLastAccessed);
        fileMetadata.setDescription(testDescription);
        fileMetadata.setTags(testTags);
        fileMetadata.setPublic(testIsPublic);
        fileMetadata.setContentEncoding(testContentEncoding);
        fileMetadata.setContentLanguage(testContentLanguage);
        fileMetadata.setExpirationDate(testExpirationDate);
        fileMetadata.setVersion(testVersion);
        
        assertEquals(testId, fileMetadata.getId());
        assertEquals(testOriginalName, fileMetadata.getOriginalName());
        assertEquals(testStoredName, fileMetadata.getStoredName());
        assertEquals(testMimeType, fileMetadata.getMimeType());
        assertEquals(testFileSize, fileMetadata.getFileSize());
        assertEquals(testChecksum, fileMetadata.getChecksum());
        assertEquals(testStorageLocation, fileMetadata.getStorageLocation());
        assertEquals(testBucketName, fileMetadata.getBucketName());
        assertEquals(testUploadedBy, fileMetadata.getUploadedBy());
        assertEquals(testUploadDate, fileMetadata.getUploadDate());
        assertEquals(testLastAccessed, fileMetadata.getLastAccessed());
        assertEquals(testDescription, fileMetadata.getDescription());
        assertEquals(testTags, fileMetadata.getTags());
        assertEquals(testIsPublic, fileMetadata.isPublic());
        assertEquals(testContentEncoding, fileMetadata.getContentEncoding());
        assertEquals(testContentLanguage, fileMetadata.getContentLanguage());
        assertEquals(testExpirationDate, fileMetadata.getExpirationDate());
        assertEquals(testVersion, fileMetadata.getVersion());
    }
}