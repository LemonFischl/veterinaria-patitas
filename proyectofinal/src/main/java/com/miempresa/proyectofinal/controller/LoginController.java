package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final RoleService roleService;

    @Autowired
    public LoginController(RoleService roleService) {
        this.roleService = roleService;
    }

    // Muestra login + registro en la misma vista
    @GetMapping("/auth/login")
    public String mostrarFormularioLogin(@RequestParam(name = "exito", required = false) String exito, Model model) {
        model.addAttribute("usuarioRegister", new Usuario()); // objeto para registro
        model.addAttribute("listaRoles", roleService.listarRoles()); // roles disponibles
        if (exito != null) {
            model.addAttribute("exito", exito);
        }
        return "auth/login";
    }

    @GetMapping("/")
    public String mostrarIndex() {
        return "index";
    }
}

