package com.miempresa.proyectofinal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Muestra el formulario de login
    @GetMapping("/auth/login")
    public String mostrarFormularioLogin() {
        return "auth/login"; // templates/auth/login.html
    }

    @GetMapping("/")
    public String mostrarIndex() {
        return "index"; // Vista principal después del login
    }
}


