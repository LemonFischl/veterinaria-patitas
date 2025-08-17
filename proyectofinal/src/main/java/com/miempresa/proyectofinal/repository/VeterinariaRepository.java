package com.miempresa.proyectofinal.repository;

import com.miempresa.proyectofinal.model.Veterinaria;
import org.springframework.data.jpa.repository.JpaRepository;

import  java.util.Optional;

public interface VeterinariaRepository extends JpaRepository <Veterinaria, Long> {
    // Método para buscar atributo único
    boolean existsByNombre(String nombre);

    Optional<Veterinaria> findByNombre(String nombre);
}
