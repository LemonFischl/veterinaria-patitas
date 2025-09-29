package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Cita;
import com.miempresa.proyectofinal.model.EstadoCita;
import com.miempresa.proyectofinal.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {
    private final CitaRepository citaRepository;

    @Autowired
    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public void guardarCita(Cita cita) {
        citaRepository.save(cita);
    }

    public List<Cita> listarCitas() {
        return citaRepository.findAll();
    }

    public void eliminarCita(Long id) {
        citaRepository.deleteById(id);
    }

    public Optional<Cita> obtenerCitaPorId(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> listarCitasPorUsuario(Long idUsuario) {
        return citaRepository.findCitasPorUsuario(idUsuario);
    }

    public boolean existePorId(Long idCita) {
        return citaRepository.existsById(idCita);
    }

    // Ejecuta todos los días cada 2 minutos
    @Scheduled(cron = "0 */2 * * * *")

    public void actualizarEstadosCita() {
        List<Cita> citas = citaRepository.findAll();
        LocalDate hoy = LocalDate.now();

        for (Cita cita : citas) {
            if (cita.getFecha() == null) continue;

            // No modificar si ya está cancelada o atendida
            if (cita.getEstado() == EstadoCita.CANCELADA || cita.getEstado() == EstadoCita.ATENDIDA) {
                continue;
            }

            // Verifica si tiene veterinario asignado
            boolean tieneVeterinario = cita.getVeterinario() != null;

            // 1. Confirmar si tiene veterinario asignado
            if (tieneVeterinario && cita.getEstado() == EstadoCita.PENDIENTE) {
                cita.setEstado(EstadoCita.CONFIRMADA);
                citaRepository.save(cita);
                continue;
            }

            // 2. Marcar como atendida si ya pasó al menos 1 día desde la fecha de la cita
            if (cita.getEstado() == EstadoCita.CONFIRMADA && cita.getFecha().isBefore(hoy)) {
                cita.setEstado(EstadoCita.ATENDIDA);
                citaRepository.save(cita);
            }

            // 3. Si no tiene veterinario asignado y está como confirmada, volverla a pendiente
            if (!tieneVeterinario && cita.getEstado() == EstadoCita.CONFIRMADA) {
                cita.setEstado(EstadoCita.PENDIENTE);
                citaRepository.save(cita);
            }
        }
    }
}