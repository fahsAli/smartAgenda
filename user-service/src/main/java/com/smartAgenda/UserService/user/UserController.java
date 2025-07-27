package com.smartAgenda.UserService.user;

import com.smartAgenda.UserService.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/sync")
    public ResponseEntity<?> syncUser(@RequestHeader("Authorization") String authHeader) {
        log.info("Received sync request");

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Invalid authorization header format");
                return ResponseEntity.status(401).body("Invalid authorization header");
            }

            String token = authHeader.substring(7);
            log.debug("Parsing JWT token");

            Claims claims = jwtUtil.parseToken(token);
            log.debug("Token parsed successfully, creating/updating user");

            UserDTO user = userService.createOrUpdateUser(claims);
            log.info("User sync completed successfully for email: {}", user.getEmail());

            return ResponseEntity.ok(user);

        } catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            return ResponseEntity.status(401).body("Token expired");
        } catch (JwtException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return ResponseEntity.status(401).body("Invalid token");
        } catch (Exception e) {
            log.error("Internal server error during user sync: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping
    public String test() {
        return "test";
    }

}