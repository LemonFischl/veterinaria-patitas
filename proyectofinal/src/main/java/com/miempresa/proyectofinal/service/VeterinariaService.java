package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Veterinaria;
import com.miempresa.proyectofinal.repository.VeterinariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinariaService {
    private final VeterinariaRepository veterinariaRepository;

    @Autowired
    public VeterinariaService(VeterinariaRepository veterinariaRepository) {
        this.veterinariaRepository = veterinariaRepository;
    }

    public void guardarVeterinaria(Veterinaria veterinaria) {
        Optional<Veterinaria> existente = veterinariaRepository.findByNombre(veterinaria.getNombre());

        if (existente.isPresent()) {
            // Si estamos creando una nueva veterinaria o editando a otra diferente con ese nombre
            if (veterinaria.getId_vet() == null || !existente.get().getId_vet().equals(veterinaria.getId_vet())) {
                throw new IllegalArgumentException("Ya existe una veterinaria con ese nombre.");
            }
        }

        veterinariaRepository.save(veterinaria);
    }


    public List<Veterinaria> listarVeterinarias() {
        return veterinariaRepository.findAll();
    }

    public void eliminarVeterinaria(Long id) {
        veterinariaRepository.deleteById(id);
    }

    public Optional<Veterinaria> obtenerVeterinariaPorId(Long id) {
        return veterinariaRepository.findById(id);
    }

    // --- Nuevos método para verificación de unicidad

    public boolean existsByNombre(String nombre) {
        return veterinariaRepository.existsByNombre(nombre);
    }
}