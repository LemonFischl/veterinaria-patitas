package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.*;
import com.miempresa.proyectofinal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Solo para usuarios con rol "CLIENTE"
@Controller
@RequestMapping("/paciente/mis-citas")
public class CitaClienteController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;

    @Autowired
    public CitaClienteController(CitaService citaService, UsuarioService usuarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public String verMisCitas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        List<Cita> citas = citaService.listarCitasPorUsuario(usuarioLogueado.getId_usuario());
        model.addAttribute("citas", citas);

        // Estados únicos
        List<String> estados = citas.stream()
                .map(c -> c.getEstado().name()) // si EstadoCita es enum
                .distinct()
                .toList();
        model.addAttribute("estados", estados);

        // Veterinarias únicas
        List<Veterinaria> veterinarias = citas.stream()
                .map(Cita::getVeterinaria)
                .filter(v -> v != null)
                .distinct()
                .toList();
        model.addAttribute("veterinariasUsuario", veterinarias);

        // Mascotas únicas
        List<Mascota> mascotas = citas.stream()
                .map(Cita::getMascota)
                .filter(m -> m != null)
                .distinct()
                .toList();
        model.addAttribute("mascotasUsuario", mascotas);

        return "paciente/mis-citas";
    }
}

