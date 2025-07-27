package com.smartAgenda.UserService.user;

import com.smartAgenda.UserService.role.Role;
import com.smartAgenda.UserService.role.RoleRepository;
import com.smartAgenda.UserService.role.RoleType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @SuppressWarnings("unchecked")
    @Transactional
    public UserDTO createOrUpdateUser(Claims claims) {
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

        User savedUser = userRepository.findByEmail(email)
                .map(user -> {
                    user.setGivenName(givenName);
                    user.setFamilyName(familyName);
                    user.setFullName(fullName);
                    user.setPictureUrl(picture);
                    user.setUpdatedAt(LocalDateTime.now());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    Role clientRole = roleRepository.findByRole(RoleType.CLIENT)
                            .orElseThrow(() -> new IllegalStateException("CLIENT role not found in database"));

                    User newUser = User.builder()
                            .email(email)
                            .givenName(givenName)
                            .familyName(familyName)
                            .fullName(fullName)
                            .pictureUrl(picture)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .status(Status.ACTIVE)
                            .roles(Set.of(clientRole))
                            .build();
                    return userRepository.save(newUser);
                });

        return userMapper.toDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return userMapper.toDTO(user);
    }
}