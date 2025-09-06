package com.miempresa.proyectofinal.repository;
import com.miempresa.proyectofinal.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c WHERE c.usuario.id_usuario = :idUsuario")
    List<Cita> findCitasPorUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.veterinario.id_veterinario = :idVeterinario")
    boolean existsByVeterinario(@Param("idVeterinario") Long idVeterinario); // Método para verificar si un veterinario tiene citas asociadas

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.mascota.id_mascota = :idMascota" )
    boolean existsByMascota(@Param("idMascota") Long idMascota); // Método para verificar si una mascota tiene citas asociadas

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.veterinaria.id_vet = :idVeterinaria" )
    boolean existsByVeterinaria(@Param("idVeterinaria") Long idVeterinaria); // Método para verificar si una veterinaria tiene citas asociadas

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.usuario.id_usuario = :idUsuario" )
    boolean existsByUsuario(@Param("idUsuario") Long idUsuario); // Método para verificar si un usuario tiene citas asociadas
}

