package com.filestorage.db;

import com.filestorage.model.FileMetadata;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(FileMetadata.class)
public interface FileMetadataDAO {
    
    @SqlUpdate("INSERT INTO file_metadata (id, original_name, stored_name, mime_type, file_size, " +
               "checksum, storage_location, bucket_name, uploaded_by, upload_date, last_accessed, " +
               "description, tags, is_public, content_encoding, content_language, expiration_date, version) " +
               "VALUES (:id, :originalName, :storedName, :mimeType, :fileSize, :checksum, " +
               ":storageLocation, :bucketName, :uploadedBy, :uploadDate, :lastAccessed, " +
               ":description, :tags, :isPublic, :contentEncoding, :contentLanguage, :expirationDate, :version)")
    void insertFileMetadata(@BindBean FileMetadata fileMetadata);
    
    default void insert(FileMetadata fileMetadata) {
        insertFileMetadata(fileMetadata);
    }
    
    @SqlUpdate("UPDATE file_metadata SET original_name = :originalName, mime_type = :mimeType, " +
               "description = :description, tags = :tags, is_public = :isPublic WHERE id = :id")
    void update(@BindBean FileMetadata fileMetadata);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE id = :id")
    Optional<FileMetadata> findById(@Bind("id") String id);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE uploaded_by = :uploadedBy ORDER BY upload_date DESC " +
              "LIMIT :limit OFFSET :offset")
    List<FileMetadata> findByUploadedBy(@Bind("uploadedBy") String uploadedBy, 
                                       @Bind("limit") int limit, 
                                       @Bind("offset") int offset);
    
    default List<FileMetadata> findByUploadedByPaginated(String uploadedBy, int page, int limit) {
        int offset = (page - 1) * limit;
        return findByUploadedBy(uploadedBy, limit, offset);
    }
    
    @SqlQuery("SELECT * FROM file_metadata WHERE mime_type = :mimeType ORDER BY upload_date DESC " +
              "LIMIT :limit OFFSET :offset")
    List<FileMetadata> findByMimeType(@Bind("mimeType") String mimeType, 
                                     @Bind("limit") int limit, 
                                     @Bind("offset") int offset);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE original_name LIKE :pattern ORDER BY upload_date DESC " +
              "LIMIT :limit OFFSET :offset")
    List<FileMetadata> searchByOriginalName(@Bind("pattern") String pattern, 
                                           @Bind("limit") int limit, 
                                           @Bind("offset") int offset);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE tags LIKE :pattern ORDER BY upload_date DESC " +
              "LIMIT :limit OFFSET :offset")
    List<FileMetadata> searchByTags(@Bind("pattern") String pattern, 
                                   @Bind("limit") int limit, 
                                   @Bind("offset") int offset);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE is_public = true ORDER BY upload_date DESC " +
              "LIMIT :limit OFFSET :offset")
    List<FileMetadata> findPublicFiles(@Bind("limit") int limit, 
                                      @Bind("offset") int offset);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE expiration_date < :currentDate")
    List<FileMetadata> findExpiredFiles(@Bind("currentDate") Date currentDate);
    
    @SqlQuery("SELECT * FROM file_metadata WHERE file_size > :minSize AND file_size < :maxSize " +
              "ORDER BY upload_date DESC LIMIT :limit OFFSET :offset")
    List<FileMetadata> findByFileSize(@Bind("minSize") long minSize, 
                                     @Bind("maxSize") long maxSize,
                                     @Bind("limit") int limit, 
                                     @Bind("offset") int offset);
    
    @SqlUpdate("UPDATE file_metadata SET last_accessed = :lastAccessed WHERE id = :id")
    void updateLastAccessed(@Bind("id") String id, @Bind("lastAccessed") Date lastAccessed);
    
    @SqlUpdate("UPDATE file_metadata SET description = :description, tags = :tags, " +
               "is_public = :isPublic WHERE id = :id")
    void updateMetadata(@Bind("id") String id, 
                       @Bind("description") String description,
                       @Bind("tags") String tags,
                       @Bind("isPublic") boolean isPublic);
    
    @SqlUpdate("UPDATE file_metadata SET expiration_date = :expirationDate WHERE id = :id")
    void updateExpirationDate(@Bind("id") String id, @Bind("expirationDate") Date expirationDate);
    
    @SqlUpdate("DELETE FROM file_metadata WHERE id = :id")
    void deleteById(@Bind("id") String id);
    
    @SqlUpdate("DELETE FROM file_metadata WHERE expiration_date < :currentDate")
    int deleteExpiredFiles(@Bind("currentDate") Date currentDate);
    
    @SqlQuery("SELECT COUNT(*) FROM file_metadata WHERE uploaded_by = :uploadedBy")
    long countByUploadedBy(@Bind("uploadedBy") String uploadedBy);
    
    @SqlQuery("SELECT SUM(file_size) FROM file_metadata WHERE uploaded_by = :uploadedBy")
    long getTotalSizeByUploadedBy(@Bind("uploadedBy") String uploadedBy);
    
    @SqlQuery("SELECT COUNT(*) FROM file_metadata WHERE mime_type = :mimeType")
    long countByMimeType(@Bind("mimeType") String mimeType);
    
    @SqlQuery("SELECT COUNT(*) FROM file_metadata WHERE is_public = true")
    long countPublicFiles();
    
    @SqlQuery("SELECT COUNT(*) FROM file_metadata")
    long countAllFiles();
    
    @SqlQuery("SELECT SUM(file_size) FROM file_metadata")
    long getTotalStorageUsed();
}