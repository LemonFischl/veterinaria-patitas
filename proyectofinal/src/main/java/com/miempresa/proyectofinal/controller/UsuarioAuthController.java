package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.Role;
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
    public String mostrarFormularioRegistro(@RequestParam(name = "exito", required = false) String exito,
                                            Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("usuarioRegister", usuario);

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

        // Validar errores del formulario
        if (result.hasErrors()) {
            model.addAttribute("listaRoles", roleService.listarRoles());
            return "auth/login";
        }

        try {
            if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
                for (Role r : roleService.listarRoles()) {
                    if ("ROLE_PACIENTE".equals(r.getName().name())) {
                        usuario.getRoles().add(r);
                        break;
                    }
                }
            }

            usuarioService.guardarUsuario(usuario);

            String mensajeExito = "¡Usuario registrado exitosamente!";
            return "redirect:/auth/login?exito=" + java.net.URLEncoder.encode(
                    mensajeExito, java.nio.charset.StandardCharsets.UTF_8);

        } catch (Exception e) {
            model.addAttribute("listaRoles", roleService.listarRoles());
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/login";
        }
    }
}
