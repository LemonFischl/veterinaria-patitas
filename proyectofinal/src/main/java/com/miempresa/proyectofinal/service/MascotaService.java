package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.EstadoCita;
import com.miempresa.proyectofinal.model.Mascota;
import com.miempresa.proyectofinal.repository.CitaRepository;
import com.miempresa.proyectofinal.repository.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MascotaService {
    private final MascotaRepository mascotaRepository;
    private final CitaRepository citaRepository;

    @Autowired
    public MascotaService(MascotaRepository mascotaRepository, CitaRepository citaRepository) {
        this.mascotaRepository = mascotaRepository;
        this.citaRepository = citaRepository;
    }

    public void guardarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }

    public List<Mascota> listarMascotas() {
        return mascotaRepository.findAll();
    }

    public void eliminarMascota(Long id) {
        boolean tieneCitas = citaRepository.existsByMascota(id);
        if (tieneCitas) {
            throw new IllegalStateException("No se puede eliminar la mascota con citas asociadas.");
        }
        mascotaRepository.deleteById(id);
    }

    public Optional<Mascota> obtenerMascotaPorId(Long id) {
        return mascotaRepository.findById(id);
    }

    public List<Mascota> obtenerMascotasConAlerta(List<Mascota> mascotas) {
        for (Mascota m : mascotas) {
            boolean tieneCitaPronta = m.getCitas() != null &&
                    m.getCitas().stream().anyMatch(cita ->
                            cita.getFecha().isBefore(LocalDate.now().plusDays(1)) &&
                                    cita.getEstado() != EstadoCita.CANCELADA &&
                                    cita.getEstado() != EstadoCita.ATENDIDA);
            m.setAlerta(tieneCitaPronta);
        }
        return mascotas;
    }

    public List<Mascota> listarMascotasPorUsuario(Long idUsuario) {
        return mascotaRepository.findMascotasPorUsuario(idUsuario);
    }


}