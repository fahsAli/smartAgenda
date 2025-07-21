package com.smartAgenda.authService.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationMillis;

    private Key signingKey;

    @PostConstruct
    public void init() {
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(OidcUser user, OAuth2AuthorizedClient client) {
        Instant now = Instant.now();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userAttributes", sanitizeAttributes(user.getAttributes()));
        claims.put("id_token", user.getIdToken().getTokenValue());
        claims.put("access_token", client.getAccessToken().getTokenValue());
        claims.put("refresh_token", client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null);
        claims.put("expires_at", Objects.requireNonNull(client.getAccessToken().getExpiresAt()).toString());

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationMillis)))
                .addClaims(claims)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> sanitizeAttributes(Map<String, Object> attributes) {
        Map<String, Object> sanitized = new HashMap<>();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Instant) {
                sanitized.put(entry.getKey(), value.toString());
            } else {
                sanitized.put(entry.getKey(), value);
            }
        }
        return sanitized;
    }
}
