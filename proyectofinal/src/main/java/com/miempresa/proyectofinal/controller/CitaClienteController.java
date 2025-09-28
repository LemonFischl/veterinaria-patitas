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
    private final VeterinariaService veterinariaService;
    private final MascotaService mascotaService;
    private final VeterinarioService veterinarioService;

    @Autowired
    public CitaClienteController(CitaService citaService, UsuarioService usuarioService, VeterinariaService veterinariaService, MascotaService mascotaService, VeterinarioService veterinarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.veterinariaService = veterinariaService;
        this.mascotaService = mascotaService;
        this.veterinarioService = veterinarioService;
    }

    @GetMapping("/listar")
    public String verMisCitas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        cargarDatosParaFormularioCita(model, usuarioLogueado);
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

        model.addAttribute("nuevaCita", new Cita());

        return "paciente/mis-citas";
    }

    @PostMapping("/guardar")
    public String guardarCita(@ModelAttribute("nuevaCita") Cita cita) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        // Asignar usuario logueado a la cita
        cita.setUsuario(usuarioLogueado);

        // Estado inicial por defecto
        cita.setEstado(EstadoCita.PENDIENTE);

        citaService.guardarCita(cita);

        // Redirigir de nuevo a la lista
        return "redirect:/paciente/mis-citas/listar";
    }

    //Carga de datos del paciente para el formulario de nueva cita
    private void cargarDatosParaFormularioCita(Model model, Usuario usuarioLogueado) {
        model.addAttribute("usuarios", List.of(usuarioLogueado));
        model.addAttribute("mascotas", mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario()));
        model.addAttribute("citas", citaService.listarCitasPorUsuario(usuarioLogueado.getId_usuario()));

        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        model.addAttribute("veterinarios", veterinarioService.listarVeterinarios());
        model.addAttribute("estados", EstadoCita.values());
    }
}

