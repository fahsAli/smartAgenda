package com.smartAgenda.UserService.config;

import com.smartAgenda.UserService.role.Role;
import com.smartAgenda.UserService.role.RoleRepository;
import com.smartAgenda.UserService.role.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (!roleRepository.existsByRole(roleType)) {
                Role role = Role.builder()
                        .role(roleType)
                        .build();
                roleRepository.save(role);
                log.info("Created role: {}", roleType);
            }
        }
    }
}
