package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.model.EstadoVet;
import com.miempresa.proyectofinal.model.Veterinaria;
import com.miempresa.proyectofinal.service.VeterinariaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class Controladores {

    @Autowired
    private VeterinariaService veterinariaService;

    @GetMapping("/veterinarias")
    public String mostrarVeterinarias(Model model) {
        List<Veterinaria> activas = veterinariaService.listarVeterinarias()
                .stream()
                .filter(v -> v.getEstadoVet() == EstadoVet.ACTIVO)
                .toList();

        model.addAttribute("veterinarias", activas);
        return "veterinarias";
    }

    @GetMapping("/contacto")
    private String contacto(){
        return "contacto";
    }
}

