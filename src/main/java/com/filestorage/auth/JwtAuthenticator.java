package com.filestorage.auth;

import io.dropwizard.auth.Authenticator;
import io.jsonwebtoken.*;

import java.security.Principal;
import java.util.Optional;

public class JwtAuthenticator implements Authenticator<String, Principal> {
    private final String secret;

    public JwtAuthenticator(String secret) {
        this.secret = secret;
    }

    @Override
    public Optional<Principal> authenticate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            
            String subject = claims.getSubject();
            String scope = claims.get("scope", String.class);
            
            if (subject != null && scope != null && 
                (scope.contains("file:read") || scope.contains("file:write"))) {
                return Optional.of(new SimplePrincipal(subject));
            }
        } catch (Exception e) {
            // Token verification failed
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