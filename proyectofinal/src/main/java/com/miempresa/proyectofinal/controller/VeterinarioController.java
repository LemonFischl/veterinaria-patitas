package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.exception.EntityNotFoundException;
import com.miempresa.proyectofinal.model.Veterinaria;
import com.miempresa.proyectofinal.model.Veterinario;
import com.miempresa.proyectofinal.service.VeterinariaService;
import com.miempresa.proyectofinal.service.VeterinarioService;
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

@Controller
@RequestMapping("/vet/veterinario")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;
    private final VeterinariaService veterinariaService;

    @Autowired
    public VeterinarioController(VeterinarioService veterinarioService, VeterinariaService veterinariaService) {
        this.veterinarioService = veterinarioService;
        this.veterinariaService = veterinariaService;
    }

    // Mostrar formulario para nuevo veterinario
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoVeterinario(Model model) {
        model.addAttribute("veterinario", new Veterinario());
        model.addAttribute("veterinarios", veterinarioService.listarVeterinarios());
        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        return "vet/veterinario";
    }

    // Guardar veterinario (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarVeterinario(@Valid @ModelAttribute Veterinario veterinario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        if (veterinario.getVeterinaria() == null || veterinario.getVeterinaria().getId_vet() == null) {
            result.rejectValue("veterinaria", "error.veterinaria", "Debe seleccionar una veterinaria.");
        }

        if (result.hasErrors()) {
            model.addAttribute("veterinarios", veterinarioService.listarVeterinarios());
            model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
            return "vet/veterinario";
        }

        // Obtener y asignar veterinaria
        Veterinaria veterinaria = veterinariaService.obtenerVeterinariaPorId(veterinario.getVeterinaria().getId_vet())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la veterinaria.", "/vet/veterinario/nuevo"));
        veterinario.setVeterinaria(veterinaria);

        veterinarioService.guardarVeterinario(veterinario);
        redirectAttributes.addFlashAttribute("exito", "El veterinario se guardó correctamente.");
        return "redirect:/vet/veterinario/nuevo";
    }

    // Eliminar veterinario
    @GetMapping("/eliminar")
    public String eliminarVeterinario(@RequestParam Long id, RedirectAttributes redirectAttributes) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (!esAdmin) {
            throw new AccessDeniedException("Solo el administrador puede eliminar veterinarios.");
        }

        try {
            veterinarioService.eliminarVeterinario(id);
            redirectAttributes.addFlashAttribute("exito", "El veterinario se eliminó correctamente.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vet/veterinario/nuevo";
    }

    // Editar veterinario
    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id, Model model) {
        Veterinario veterinario = veterinarioService.obtenerVeterinarioPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el veterinario.", "/vet/veterinario/nuevo"));
        model.addAttribute("veterinario", veterinario);
        model.addAttribute("veterinarios", veterinarioService.listarVeterinarios());
        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        return "vet/veterinario";
    }
}