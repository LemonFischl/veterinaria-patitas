package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.exception.EntityNotFoundException;
import com.miempresa.proyectofinal.model.*;
import com.miempresa.proyectofinal.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.OptimisticLockException;
import com.miempresa.proyectofinal.security.SecurityUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        model.addAttribute("citas", citaService.listarCitasPorUsuario(usuarioLogueado.getId_usuario()));
        return "paciente/mis-citas";
    }
}

