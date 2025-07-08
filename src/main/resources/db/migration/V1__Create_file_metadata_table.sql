CREATE TABLE file_metadata (
    id VARCHAR(36) PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    checksum VARCHAR(64),
    storage_location VARCHAR(500) NOT NULL,
    bucket_name VARCHAR(100),
    uploaded_by VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_accessed TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    tags TEXT,
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    content_encoding VARCHAR(50),
    content_language VARCHAR(20),
    expiration_date TIMESTAMP,
    version VARCHAR(20) NOT NULL DEFAULT '1.0'
);

-- Indexes for common query patterns
CREATE INDEX idx_file_metadata_uploaded_by ON file_metadata(uploaded_by);
CREATE INDEX idx_file_metadata_mime_type ON file_metadata(mime_type);
CREATE INDEX idx_file_metadata_upload_date ON file_metadata(upload_date);
CREATE INDEX idx_file_metadata_is_public ON file_metadata(is_public);
CREATE INDEX idx_file_metadata_expiration_date ON file_metadata(expiration_date);
CREATE INDEX idx_file_metadata_file_size ON file_metadata(file_size);
CREATE INDEX idx_file_metadata_stored_name ON file_metadata(stored_name);

-- Full-text search indexes
CREATE FULLTEXT INDEX idx_file_metadata_original_name ON file_metadata(original_name);
CREATE FULLTEXT INDEX idx_file_metadata_tags ON file_metadata(tags);
CREATE FULLTEXT INDEX idx_file_metadata_description ON file_metadata(description);