package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.EstadoVet;
import jakarta.validation.Valid;

import com.miempresa.proyectofinal.model.Veterinaria;
import com.miempresa.proyectofinal.service.VeterinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/vet/veterinaria")
public class VeterinariaController {

    private final VeterinariaService veterinariaService;

    @Autowired
    public VeterinariaController(VeterinariaService veterinariaService) {
        this.veterinariaService = veterinariaService;
    }

    // Mostrar formulario para nueva veterinaria y listar las existentes
    // Este método maneja la carga inicial de la página y la visualización de la lista.
    @GetMapping("/nuevo")
    public String mostrarFormularioConLista(Model model) {
        // Objeto Veterinaria vacío para el formulario (para nuevo registro)
        model.addAttribute("veterinaria", new Veterinaria());
        // Lista de todas las veterinarias para la tabla
        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        return "vet/veterinaria"; // Retorna el nombre de la vista Thymeleaf
    }

    // Guardar o actualizar una veterinaria
    @PostMapping("/guardar")
    public String guardarVeterinaria(@Valid @ModelAttribute("veterinaria") Veterinaria veterinaria,
                                     BindingResult result,
                                     Model model) { // Usamos RedirectAttributes

        // Si hay errores de validación, redirige y muestra un mensaje genérico.
        // Los errores específicos de campo no se mostrarán después de una redirección con esta estrategia.
        if (result.hasErrors()) { // (Manejo de errores de validación)
            model.addAttribute("veterinarias", veterinariaService.listarVeterinarias()); // (Recargar lista para la vista)
            model.addAttribute("estados", EstadoVet.values()); // necesario para el <select>
            return "vet/veterinaria"; //(Volver al formulario con errores)
        }
        // Validación manual: nombre duplicado (solo si es nuevo)
        if (veterinaria.getId_vet() == null && veterinariaService.existsByNombre(veterinaria.getNombre())) {
            model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
            model.addAttribute("estados", EstadoVet.values());
            model.addAttribute("error", "Ya existe una veterinaria con ese nombre.");
            return "vet/veterinaria";
        }

        // Validación de nombre duplicado en edición
        if (veterinaria.getId_vet() != null) {
            Veterinaria existente = veterinariaService.obtenerVeterinariaPorId(veterinaria.getId_vet());
            if (existente != null && !existente.getNombre().equals(veterinaria.getNombre())
                    && veterinariaService.existsByNombre(veterinaria.getNombre())) {
                model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
                model.addAttribute("estados", EstadoVet.values());
                model.addAttribute("error", "Ya existe otra veterinaria con ese nombre.");
                return "vet/veterinaria";
            }
        }

        // Guardar si todo está bien
        veterinariaService.guardarVeterinaria(veterinaria);
        return "redirect:/vet/veterinaria/nuevo";
    }

    // Método para sólo listar
    @GetMapping("/listar")
    public String listarVeterinarias(Model model) {
        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        return "vet/veterinaria";
    }


    // Eliminar veterinaria por ID
    @GetMapping("/eliminar")
    public String eliminarVeterinaria(@RequestParam Long id) {
        veterinariaService.eliminarVeterinaria(id);
        return "redirect:/vet/veterinaria/nuevo";
    }

    // Mostrar formulario para editar una veterinaria existente
    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id, Model model) {
        Veterinaria veterinaria = veterinariaService.obtenerVeterinariaPorId(id);
        model.addAttribute("veterinaria", veterinaria);
        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        return "vet/veterinaria";
    }
}