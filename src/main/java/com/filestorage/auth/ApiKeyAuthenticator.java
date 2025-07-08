package com.filestorage.auth;

import io.dropwizard.auth.Authenticator;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public class ApiKeyAuthenticator implements Authenticator<String, Principal> {
    private final List<String> validApiKeys;

    public ApiKeyAuthenticator(List<String> validApiKeys) {
        this.validApiKeys = validApiKeys;
    }

    @Override
    public Optional<Principal> authenticate(String apiKey) {
        if (apiKey != null && validApiKeys.contains(apiKey)) {
            return Optional.of(new SimplePrincipal("api-user"));
        }
        return Optional.empty();
    }
    
    private static class SimplePrincipal implements Principal {
        private final String name;
        
        public SimplePrincipal(String name) {
            this.name = name;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}