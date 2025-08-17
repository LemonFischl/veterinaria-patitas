package com.miempresa.proyectofinal.repository;

import com.miempresa.proyectofinal.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeterinarioRepository extends JpaRepository <Veterinario, Long> {
}