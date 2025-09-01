package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.exception.EntityNotFoundException;
import com.miempresa.proyectofinal.model.RoleName;
import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.model.Mascota;
import com.miempresa.proyectofinal.security.SecurityUtils;
import com.miempresa.proyectofinal.service.MascotaService;
import com.miempresa.proyectofinal.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.util.List;


@Controller
@RequestMapping("/paciente/mascota")
public class MascotaController {

    private final MascotaService mascotaService;
    private final UsuarioService usuarioService;

    @Autowired
    public MascotaController(MascotaService mascotaService, UsuarioService usuarioService) {
        this.mascotaService = mascotaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo")
    public String listarMascotas(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(username);

        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        model.addAttribute("mascota", new Mascota());

        if (esPaciente) {
            List<Mascota> propias = mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario());
            List<Mascota> conAlerta = mascotaService.obtenerMascotasConAlerta(propias);
            model.addAttribute("mascotas", conAlerta);
            model.addAttribute("usuarios", List.of(usuarioLogueado));
        } else {
            List<Mascota> todas = mascotaService.listarMascotas();
            List<Mascota> conAlerta = mascotaService.obtenerMascotasConAlerta(todas);
            model.addAttribute("mascotas", conAlerta);
            model.addAttribute("usuarios", usuarioService.listarUsuariosConRol(RoleName.ROLE_PACIENTE));
        }

        return "paciente/mascota";
    }

    @PostMapping("/guardar")
    public String guardarMascota(@Valid @ModelAttribute Mascota mascota, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());
        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente) {
            mascota.setUsuario(usuarioLogueado);
        } else {
            if (mascota.getUsuario() == null || mascota.getUsuario().getId_usuario() == null) {
                result.rejectValue("usuario", "error.usuario", "Debe seleccionar un dueño.");
            }
        }

        if (result.hasErrors()) {
            List<Mascota> mascotas = esPaciente
                    ? mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario())
                    : mascotaService.listarMascotas();

            model.addAttribute("mascotas", mascotaService.obtenerMascotasConAlerta(mascotas));
            model.addAttribute("usuarios", esPaciente
                    ? List.of(usuarioLogueado)
                    : usuarioService.listarUsuariosConRol(RoleName.ROLE_PACIENTE));
            model.addAttribute("error", "Revisa los campos del formulario.");
            return "paciente/mascota";
        }

        mascotaService.guardarMascota(mascota);
        redirectAttributes.addFlashAttribute("exito", "La mascota se guardó correctamente.");
        return "redirect:/paciente/mascota/nuevo";
    }

    @GetMapping("/eliminar")
    public String eliminarMascota(@RequestParam Long id, RedirectAttributes redirectAttributes) throws AccessDeniedException {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la mascota.", "/paciente/mascota/nuevo"));

        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());
        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente && !mascota.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario())) {
            throw new AccessDeniedException("No puedes eliminar esta mascota.");
        }

        mascotaService.eliminarMascota(id);
        redirectAttributes.addFlashAttribute("exito", "La mascota se eliminó correctamente.");
        return "redirect:/paciente/mascota/nuevo";
    }

    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id, Model model) throws AccessDeniedException {
        Mascota mascota = mascotaService.obtenerMascotaPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la mascota.", "/paciente/mascota/nuevo"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());
        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente && !mascota.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario())) {
            throw new AccessDeniedException("No puedes editar esta mascota.");
        }

        model.addAttribute("mascota", mascota);
        model.addAttribute("mascotas", mascotaService.obtenerMascotasConAlerta(
                esPaciente ? mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario())
                        : mascotaService.listarMascotas()));
        model.addAttribute("usuarios", esPaciente
                ? List.of(usuarioLogueado)
                : usuarioService.listarUsuariosConRol(RoleName.ROLE_PACIENTE));

        return "paciente/mascota";
    }
}