package com.smartAgenda.UserService.user;

import com.smartAgenda.UserService.role.RoleType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String givenName;
    private String familyName;
    private String email;
    private String pictureUrl;
    private String fullName;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<RoleType> roles;
}