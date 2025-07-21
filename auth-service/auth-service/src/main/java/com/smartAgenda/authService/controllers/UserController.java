package com.smartAgenda.authService.controllers;

import com.smartAgenda.authService.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/user")
    public Map<String, String> getJwt(Authentication authentication) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName()
        );
        OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();
        String jwt = jwtService.createToken(oidcUser, client);
        return Map.of("jwt", jwt);
    }
}
