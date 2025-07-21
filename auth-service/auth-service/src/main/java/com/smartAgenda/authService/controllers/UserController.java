package com.smartAgenda.authService.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public UserController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // Get authorized client (contains access & refresh tokens)
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );

        // Get OIDC user info
        OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();

        Map<String, Object> result = new HashMap<>();
        result.put("userAttributes", oidcUser.getAttributes());
        result.put("id_token", oidcUser.getIdToken().getTokenValue());
        result.put("access_token", client.getAccessToken().getTokenValue());
        result.put("refresh_token", client.getRefreshToken() != null ? client.getRefreshToken().getTokenValue() : null);
        result.put("expires_at", client.getAccessToken().getExpiresAt());

        return result;
    }
}

