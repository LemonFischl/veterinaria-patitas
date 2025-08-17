package com.miempresa.proyectofinal.repository;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.model.RoleName;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRegisterRepository extends JpaRepository<Usuario, Long> {
    Boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    // --- NUEVOS MÉTODOS REQUERIDOS ---
    boolean existsByUsername(String username); // Para verificar si un username ya existe
    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByUsername(String username); // Para buscar un usuario por su username

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.name = :rol")
    List<Usuario> findUsuariosByRol(@Param("rol") RoleName rol);

}
