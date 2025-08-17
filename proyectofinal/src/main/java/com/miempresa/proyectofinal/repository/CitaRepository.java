package com.miempresa.proyectofinal.repository;
import com.miempresa.proyectofinal.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c WHERE c.usuario.id_usuario = :idUsuario")
    List<Cita> findCitasPorUsuario(@Param("idUsuario") Long idUsuario);
}

