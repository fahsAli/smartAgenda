package com.smartAgenda.authService.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        // Return the OAuth2User attributes (user info) as JSON
        return principal.getAttributes();
    }
}
