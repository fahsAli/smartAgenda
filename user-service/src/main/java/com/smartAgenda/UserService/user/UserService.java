package com.smartAgenda.UserService.user;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public User createOrUpdateUser(Claims claims) {
        Map<String, Object> userAttributes = (Map<String, Object>) claims.get("userAttributes", Map.class);

        if (userAttributes == null) {
            throw new IllegalArgumentException("Token does not contain userAttributes");
        }

        final String emailRaw = (String) userAttributes.get("email");
        if (emailRaw == null) {
            throw new IllegalArgumentException("Token userAttributes do not contain email");
        }
        final String email = emailRaw.toLowerCase();

        final String givenName = (String) userAttributes.get("given_name");
        final String familyName = (String) userAttributes.get("family_name");
        final String fullName = (String) userAttributes.get("name");
        final String picture = (String) userAttributes.get("picture");

        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setGivenName(givenName);
                    user.setFamilyName(familyName);
                    user.setFullName(fullName);
                    user.setPictureUrl(picture);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .givenName(givenName)
                            .familyName(familyName)
                            .fullName(fullName)
                            .pictureUrl(picture)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .status(Status.ACTIVE)
                            .build();
                    return userRepository.save(newUser);
                });
    }


}
