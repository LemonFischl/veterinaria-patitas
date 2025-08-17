package com.miempresa.proyectofinal.repository;

import com.miempresa.proyectofinal.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    @Query("SELECT m FROM Mascota m WHERE m.usuario.id_usuario = :idUsuario")
    List<Mascota> findMascotasPorUsuario(@Param("idUsuario") Long idUsuario);
}