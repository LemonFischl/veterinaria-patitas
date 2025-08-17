package com.miempresa.proyectofinal.repository;

import com.miempresa.proyectofinal.model.Role;
import com.miempresa.proyectofinal.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
