package com.miempresa.proyectofinal.config;

import com.miempresa.proyectofinal.model.Role;
import com.miempresa.proyectofinal.model.RoleName;
import com.miempresa.proyectofinal.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            for (RoleName roleName : RoleName.values()) {
                if (!roleRepository.findByName(roleName).isPresent()) {
                    roleRepository.save(new Role(roleName));
                }
            }
        };
    }
}
