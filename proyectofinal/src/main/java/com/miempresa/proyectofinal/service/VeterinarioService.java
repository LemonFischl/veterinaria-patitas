package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Veterinario;
import com.miempresa.proyectofinal.repository.VeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinarioService {
    private final VeterinarioRepository veterinarioRepository;

    @Autowired
    public VeterinarioService(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    public void guardarVeterinario(Veterinario veterinario) {
        veterinarioRepository.save(veterinario);
    }

    public List<Veterinario> listarVeterinarios() {
        return veterinarioRepository.findAll();
    }

    public void eliminarVeterinario(Long id) {
        veterinarioRepository.deleteById(id);
    }

    public Veterinario obtenerVeterinarioPorId(Long id) {
        return veterinarioRepository.findById(id).orElse(null);
    }

}