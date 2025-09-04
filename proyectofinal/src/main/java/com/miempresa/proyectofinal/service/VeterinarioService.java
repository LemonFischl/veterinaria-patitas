package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Veterinario;
import com.miempresa.proyectofinal.repository.CitaRepository;
import com.miempresa.proyectofinal.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;
    private final CitaRepository citaRepository;

    @Autowired
    public VeterinarioService(VeterinarioRepository veterinarioRepository, CitaRepository citaRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.citaRepository = citaRepository;
    }

    public void guardarVeterinario(Veterinario veterinario) {
        veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> listarVeterinarios() {
        return veterinarioRepository.findAll();
    }

    public void eliminarVeterinario(Long id) {
        boolean tieneCitas = citaRepository.existsByVeterinario(id);
        if (tieneCitas) {
            throw new IllegalStateException("No se puede eliminar el veterinario con citas asociadas.");
        }

        veterinarioRepository.deleteById(id);
    }

    public Optional<Veterinario> obtenerVeterinarioPorId(Long id) {
        return veterinarioRepository.findById(id);
    }

}