package com.filestorage.resources;

import com.filestorage.db.FileMetadataDAO;
import com.filestorage.model.FileMetadata;
import io.dropwizard.auth.Auth;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Path("/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class MetadataResource {
    private final FileMetadataDAO fileMetadataDAO;

    public MetadataResource(FileMetadataDAO fileMetadataDAO) {
        this.fileMetadataDAO = fileMetadataDAO;
    }

    @GET
    @Path("/{fileId}")
    public Response getFileMetadata(@Auth Principal user, @PathParam("fileId") String fileId) {
        Optional<FileMetadata> metadata = fileMetadataDAO.findById(fileId);
        if (metadata.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(metadata.get()).build();
    }

    @GET
    public Response getUserFiles(@Auth Principal user, 
                                 @QueryParam("page") @DefaultValue("1") int page,
                                 @QueryParam("limit") @DefaultValue("10") int limit) {
        List<FileMetadata> files = fileMetadataDAO.findByUploadedByPaginated(user.getName(), page, limit);
        return Response.ok(files).build();
    }

    @PUT
    @Path("/{fileId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFileMetadata(@Auth Principal user, 
                                       @PathParam("fileId") String fileId,
                                       FileMetadata updatedMetadata) {
        Optional<FileMetadata> existingMetadata = fileMetadataDAO.findById(fileId);
        if (existingMetadata.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        FileMetadata metadata = existingMetadata.get();
        if (updatedMetadata.getFileName() != null) {
            metadata.setFileName(updatedMetadata.getFileName());
        }
        if (updatedMetadata.getContentType() != null) {
            metadata.setContentType(updatedMetadata.getContentType());
        }

        fileMetadataDAO.update(metadata);
        return Response.ok(metadata).build();
    }
}