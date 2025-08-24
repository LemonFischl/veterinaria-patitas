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
@RequestMapping("/admin/usuario")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;
    private final RoleService roleService;

    @Autowired
    public UsuarioAdminController(UsuarioService usuarioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.roleService = roleService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(@RequestParam(name = "exito", required = false) String exito, Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("listaRoles", roleService.listarRoles());
        model.addAttribute("usuarios", usuarioService.listarUsuarios()); // Para mostrar la tabla

        if (exito != null) {
            model.addAttribute("exito", exito);
        }

        return "admin/usuario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
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
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            return "admin/usuario";
        }

        try {
            usuarioService.guardarUsuario(usuario);

            String mensajeExito = "¡Usuario registrado exitosamente!";
            return "redirect:/admin/usuario/nuevo?exito=" +
                    java.net.URLEncoder.encode(mensajeExito, java.nio.charset.StandardCharsets.UTF_8);

        } catch (Exception e) {
            model.addAttribute("listaRoles", roleService.listarRoles());
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "admin/usuario";
        }
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("listaRoles", roleService.listarRoles());
        return "admin/usuario";
    }

    @GetMapping("/eliminar")
    public String eliminarUsuario(@RequestParam Long id) {
        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuario/nuevo";
    }
}
