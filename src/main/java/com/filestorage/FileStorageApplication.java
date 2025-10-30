package com.filestorage;

import com.filestorage.config.FileStorageConfiguration;
import com.filestorage.health.DatabaseHealthCheck;
import com.filestorage.health.S3HealthCheck;
import com.filestorage.resources.FileResource;
import com.filestorage.resources.MetadataResource;
import com.filestorage.auth.JwtAuthFilter;

import com.filestorage.db.FileMetadataDAO;
import com.filestorage.service.FileStorageService;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.container.ContainerRequestFilter;
import java.security.Principal;
import java.util.List;

public class FileStorageApplication extends Application<FileStorageConfiguration> {
    
    public static void main(String[] args) throws Exception {
        new FileStorageApplication().run(args);
    }

    @Override
    public String getName() {
        return "file-storage";
    }

    @Override
    public void initialize(Bootstrap<FileStorageConfiguration> bootstrap) {
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(FileStorageConfiguration configuration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        
        final FileMetadataDAO fileMetadataDAO = jdbi.onDemand(FileMetadataDAO.class);
        final FileStorageService fileStorageService = new FileStorageService(
            configuration.getS3Config(), 
            configuration.getLocalStorageConfig()
        );
        
        // Setup authentication chain using legacy patterns
        final ContainerRequestFilter jwtFilter = new JwtAuthFilter.Builder<Principal>()
            .setAuthenticator(new com.filestorage.auth.JwtAuthenticator(configuration.getJwtSecret()))
            .setPrefix("Bearer")
            .buildAuthFilter();
            
        environment.jersey().register(new AuthDynamicFeature(jwtFilter));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(Principal.class));
        
        // Register resources
        environment.jersey().register(new FileResource(fileMetadataDAO, fileStorageService));
        environment.jersey().register(new MetadataResource(fileMetadataDAO));
        
        // Register health checks
        environment.healthChecks().register("database", new DatabaseHealthCheck(jdbi));
        environment.healthChecks().register("s3", new S3HealthCheck(configuration.getS3Config()));
    }
}