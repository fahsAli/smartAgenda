package com.smartAgenda.UserService.user;

import com.smartAgenda.UserService.role.Role;
import com.smartAgenda.UserService.role.RoleType;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        Set<RoleType> roleTypes = user.getRoles() != null
                ? user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet())
                : Set.of();

        return UserDTO.builder()
                .id(user.getId())
                .givenName(user.getGivenName())
                .familyName(user.getFamilyName())
                .email(user.getEmail())
                .pictureUrl(user.getPictureUrl())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(roleTypes)
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .givenName(userDTO.getGivenName())
                .familyName(userDTO.getFamilyName())
                .email(userDTO.getEmail())
                .pictureUrl(userDTO.getPictureUrl())
                .fullName(userDTO.getFullName())
                .status(userDTO.getStatus())
                .createdAt(userDTO.getCreatedAt())
                .updatedAt(userDTO.getUpdatedAt())
                .build();
    }
}
