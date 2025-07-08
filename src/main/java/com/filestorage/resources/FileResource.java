package com.filestorage.resources;

import com.filestorage.db.FileMetadataDAO;
import com.filestorage.model.FileMetadata;
import com.filestorage.service.FileStorageService;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

@Path("/files")
@Produces(MediaType.APPLICATION_JSON)
public class FileResource {
    private final FileMetadataDAO fileMetadataDAO;
    private final FileStorageService fileStorageService;

    public FileResource(FileMetadataDAO fileMetadataDAO, FileStorageService fileStorageService) {
        this.fileMetadataDAO = fileMetadataDAO;
        this.fileStorageService = fileStorageService;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Auth Principal user,
                               @FormDataParam("file") InputStream fileInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail) {
        try {
            FileMetadata metadata = fileStorageService.uploadFile(fileInputStream, fileDetail.getFileName(), user.getName());
            fileMetadataDAO.insert(metadata);
            return Response.ok(metadata).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("File upload failed: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{fileId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@Auth Principal user, @PathParam("fileId") String fileId) {
        Optional<FileMetadata> metadata = fileMetadataDAO.findById(fileId);
        if (metadata.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            InputStream fileStream = fileStorageService.downloadFile(metadata.get().getStoragePath());
            StreamingOutput output = outputStream -> {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                fileStream.close();
            };

            return Response.ok(output)
                    .header("Content-Disposition", "attachment; filename=\"" + metadata.get().getFileName() + "\"")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("File download failed: " + e.getMessage())
                    .build();
        }
    }

    @DELETE
    @Path("/{fileId}")
    public Response deleteFile(@Auth Principal user, @PathParam("fileId") String fileId) {
        Optional<FileMetadata> metadata = fileMetadataDAO.findById(fileId);
        if (metadata.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            fileStorageService.deleteFile(metadata.get().getStoragePath());
            fileMetadataDAO.deleteById(fileId);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("File deletion failed: " + e.getMessage())
                    .build();
        }
    }
}