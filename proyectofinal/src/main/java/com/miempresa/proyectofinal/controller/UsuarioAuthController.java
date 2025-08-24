package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.service.RoleService;
import com.miempresa.proyectofinal.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class UsuarioAuthController {
    private final UsuarioService usuarioService;
    private final RoleService roleService;

    @Autowired
    public UsuarioAuthController(UsuarioService usuarioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.roleService = roleService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(@RequestParam(name = "exito", required = false) String exito, Model model) {
        model.addAttribute("usuarioRegister", new Usuario());
        model.addAttribute("listaRoles", roleService.listarRoles());
        if (exito != null) {
            model.addAttribute("exito", exito);
        }
        return "auth/login";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioRegister") Usuario usuario,
                                 BindingResult result,
                                 Model model) {

        // VALIDACIONES
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            result.rejectValue("roles", "error.usuario", "Seleccione al menos un rol.");
        }

        if (usuarioService.existeUsername(usuario)) {
            result.rejectValue("username", "error.usuario", "El nombre de usuario ya está ocupado.");
        }

        if (usuarioService.existeEmail(usuario)) {
            result.rejectValue("email", "error.usuario", "El correo electrónico ya está registrado.");
        }

        if (result.hasErrors()) {
            model.addAttribute("listaRoles", roleService.listarRoles());
            return "auth/login";
        }

        try {
            // Guardar usuario
            usuarioService.guardarUsuario(usuario);

            String mensajeExito = "¡Usuario registrado exitosamente!";
            return "redirect:/auth/login?exito=" + java.net.URLEncoder.encode(mensajeExito, java.nio.charset.StandardCharsets.UTF_8);

        } catch (Exception e) {
            model.addAttribute("listaRoles", roleService.listarRoles());
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/login";
        }
    }
}