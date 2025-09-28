package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.service.MascotaService;
import com.miempresa.proyectofinal.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Solo para usuarios con rol "CLIENTE"
@Controller
@RequestMapping("/paciente/mis-mascotas")
public class MascotasClienteController {
private final MascotaService mascotaService;
private final UsuarioService usuarioService;

    public MascotasClienteController(MascotaService mascotaService, UsuarioService usuarioService) {
        this.mascotaService = mascotaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/listar")
    public String verMisMascotas(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        model.addAttribute("mascotas", mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario()));
        return "paciente/mis-mascotas";
    }
}

