package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.model.Role;
import com.miempresa.proyectofinal.service.RoleService;
import com.miempresa.proyectofinal.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

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
    public String mostrarFormularioRegistro(@RequestParam(name = "exito", required = false) String exito,
                                            Model model) {
        model.addAttribute("usuarioRegister", new Usuario());
        if (exito != null) {
            model.addAttribute("exito", exito);
        }
        return "auth/login";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuarioRegister") Usuario usuario,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            return "auth/login";
        }

        try {
            Role rolPaciente = roleService.listarRoles().stream()
                    .filter(r -> r.getName().name().equals("ROLE_PACIENTE"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Rol PACIENTE no encontrado"));

            usuario.setRoles(Set.of(rolPaciente));

            usuarioService.guardarUsuario(usuario);

            String mensajeExito = "¡Usuario registrado exitosamente!";
            return "redirect:/auth/login?exito=" + URLEncoder.encode(mensajeExito, StandardCharsets.UTF_8);

        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/login";
        }
    }
}
