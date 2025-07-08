# File Storage Service

A Dropwizard-based REST API for file storage and management with support for local and S3 storage.

## Overview

This service provides a comprehensive file storage solution with:
- File upload and download capabilities
- Metadata management and search
- Support for both local and S3 storage
- JWT and API key authentication
- File type validation and size limits
- Expiration and cleanup capabilities

## Technology Stack

- **Framework**: Dropwizard 2.0.x
- **Java**: Java 11
- **Build Tool**: Gradle 6.9
- **Database**: MySQL with Flyway migrations
- **Storage**: Local filesystem and Amazon S3
- **Authentication**: JWT + API Key
- **File Processing**: Apache Tika
- **Logging**: Logback
- **Testing**: JUnit 4

## API Endpoints

### Authentication
- JWT Token: `Authorization: Bearer <token>`
- API Key: `X-API-Key: <key>`

### File Operations

#### Upload File
```bash
POST /api/v1/files/upload
Content-Type: multipart/form-data

file=@document.pdf
description="Important document"
tags="document,pdf,important"
isPublic=false
```

#### Download File
```bash
GET /api/v1/files/{fileId}/download
```

#### Get File Metadata
```bash
GET /api/v1/files/{fileId}/metadata
```

#### Update File Metadata
```bash
PUT /api/v1/files/{fileId}/metadata
Content-Type: application/json

{
  "description": "Updated description",
  "tags": "updated,tags",
  "isPublic": true
}
```

#### Delete File
```bash
DELETE /api/v1/files/{fileId}
```

### Search and Listing

#### List User Files
```bash
GET /api/v1/files/user/{userId}?offset=0&limit=20
```

#### Search Files by Name
```bash
GET /api/v1/files/search/name?q=document&offset=0&limit=20
```

#### Search Files by Tags
```bash
GET /api/v1/files/search/tags?q=important&offset=0&limit=20
```

#### List Files by Type
```bash
GET /api/v1/files/type/{mimeType}?offset=0&limit=20
```

#### Get Public Files
```bash
GET /api/v1/files/public?offset=0&limit=20
```

### Statistics

#### Get Storage Statistics
```bash
GET /api/v1/files/stats
```

#### Get User Statistics
```bash
GET /api/v1/files/stats/user/{userId}
```

## Setup Instructions

### Prerequisites
- Java 11
- MySQL 8.0
- Gradle 6.9
- AWS S3 (optional)

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE file_storage;
CREATE USER 'storage_user'@'localhost' IDENTIFIED BY 'storage_pass';
GRANT ALL PRIVILEGES ON file_storage.* TO 'storage_user'@'localhost';
FLUSH PRIVILEGES;
```

2. Run database migrations:
```bash
./gradlew flywayMigrate
```

### Running the Application

1. Build the project:
```bash
./gradlew build
```

2. Run the application:
```bash
./gradlew run
```

The API will be available at:
- Main API: `http://localhost:8080`
- Admin: `http://localhost:8081`

## Configuration

### Storage Configuration

#### Local Storage
```yaml
localStorage:
  basePath: "/tmp/file-storage"
  enabled: true
```

#### S3 Storage
```yaml
s3:
  accessKey: "your-access-key"
  secretKey: "your-secret-key"
  bucketName: "your-bucket-name"
  region: "us-east-1"
  endpoint: null  # Optional for S3-compatible services
  enabled: true
```

### File Upload Limits
```yaml
fileUploadLimits:
  maxFileSize: 104857600  # 100MB
  maxTotalSize: 1073741824  # 1GB
  allowedMimeTypes:
    - "image/jpeg"
    - "image/png"
    - "application/pdf"
    - "text/plain"
```

## File Lifecycle

1. **Upload**: Files are uploaded via multipart form data
2. **Storage**: Files are stored in local filesystem or S3
3. **Metadata**: File metadata is stored in MySQL database
4. **Access**: Files can be downloaded or accessed via API
5. **Expiration**: Files can have expiration dates for automatic cleanup
6. **Deletion**: Files can be manually deleted or expire automatically

## Security Features

- JWT-based authentication
- API key authentication
- File type validation
- Size limits enforcement
- Public/private file access control
- Input validation and sanitization

## File Processing

- MIME type detection using Apache Tika
- Checksum generation for integrity verification
- Metadata extraction from uploaded files
- File size and type validation

## Storage Options

### Local Storage
- Files stored in configurable local directory
- Suitable for development and small deployments
- Direct filesystem access

### S3 Storage
- Files stored in Amazon S3 or compatible services
- Scalable and durable storage
- Support for custom endpoints (MinIO, etc.)

## Health Checks

- Database connectivity: `http://localhost:8081/healthcheck`
- S3 connectivity: `http://localhost:8081/healthcheck`

## Monitoring

- Application metrics: `http://localhost:8081/metrics`
- File upload/download statistics
- Storage usage tracking

## Testing

Run all tests:
```bash
./gradlew test
```

## Docker Deployment

### Build Docker Image
```bash
docker build -t file-storage-service .
```

### Run with Docker Compose
```bash
docker-compose up -d
```

## API Keys

Default API keys for testing:
- `storage-api-key-001`
- `storage-api-key-002`

## File Expiration

Files can have expiration dates set:
- Automatic cleanup of expired files
- Configurable cleanup schedule
- Expired file detection API

## Supported File Types

Default supported MIME types:
- Images: JPEG, PNG, GIF, WebP
- Documents: PDF, Plain Text, JSON, XML
- Archives: ZIP
- Binary: application/octet-stream

## Performance Features

- Database connection pooling
- Efficient file streaming
- Metadata caching
- Batch operations support
- Optimized database indexes

## Error Handling

- Comprehensive error messages
- HTTP status code compliance
- Detailed logging
- Graceful failure handling

## Troubleshooting

### Common Issues

1. **File Upload Fails**
   - Check file size limits
   - Verify MIME type is allowed
   - Ensure sufficient disk space

2. **S3 Connection Issues**
   - Verify credentials
   - Check bucket permissions
   - Validate region settings

3. **Database Connection Failed**
   - Check MySQL is running
   - Verify connection details
   - Ensure database exists

## Contributing

1. Fork the repository
2. Create feature branch
3. Make changes with tests
4. Run tests and ensure they pass
5. Submit pull request