package com.miempresa.proyectofinal.controller;

import com.miempresa.proyectofinal.exception.EntityNotFoundException;
import com.miempresa.proyectofinal.model.*;
import com.miempresa.proyectofinal.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.OptimisticLockException;
import com.miempresa.proyectofinal.security.SecurityUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/cita")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioService usuarioService;
    private final MascotaService mascotaService;
    private final VeterinariaService veterinariaService;
    private final VeterinarioService veterinarioService;

    @Autowired
    public CitaController(CitaService citaService,
                          UsuarioService usuarioService,
                          MascotaService mascotaService,
                          VeterinariaService veterinariaService,
                          VeterinarioService veterinarioService) {
        this.citaService = citaService;
        this.usuarioService = usuarioService;
        this.mascotaService = mascotaService;
        this.veterinariaService = veterinariaService;
        this.veterinarioService = veterinarioService;
    }

    // Muestra el formulario para registrar una nueva cita
    @GetMapping("/nuevo")
    public String mostrarFormularioCita(@RequestParam(value = "exito", required = false) String exito,
                                        @RequestParam(value = "denegado", required = false) String denegado,
                                        Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuario = usuarioService.buscarPorUsername(username);

        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        Cita cita = new Cita();
        if (esPaciente) {
            cita.setUsuario(usuario);  // Aquí se asegura que el hidden tendrá el valor
        }
        model.addAttribute("cita", cita);
        cargarDatosParaFormularioCita(model, usuario, esPaciente);

        if (exito != null) model.addAttribute("exito", exito);
        if (denegado != null) model.addAttribute("error", denegado);

        return "admin/cita";
    }

    // Guarda la cita
    @PostMapping("/guardar")
    public String guardarCita(@Valid @ModelAttribute("cita") Cita cita,
                              BindingResult result,
                              Model model, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());
        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente && !cita.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario())) {
            // Prevención de registro fraudulento
            result.rejectValue("usuario.id_usuario", "error.usuario", "No puedes registrar una cita para otro usuario.");
        }

        // Para guardar luego de modificar
        if (esPaciente && cita.getId_cita() != null) {
            Cita citaExistente = citaService.obtenerCitaPorId(cita.getId_cita())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la cita.", "/admin/cita/nuevo"));;

            if (!citaExistente.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario()) ||
                    !(citaExistente.getEstado() == EstadoCita.PENDIENTE || citaExistente.getEstado() == EstadoCita.CONFIRMADA)) {
                String mensaje = "No puedes modificar esta cita.";
                return "redirect:/admin/cita/nuevo?denegado=" +
                        java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
            }
        }

        if (esPaciente) {
            // Forzar estado PENDIENTE
            cita.setEstado(EstadoCita.PENDIENTE);
        }

        if (result.hasErrors()) {
            System.out.println("Errores en el formulario:");
            cargarDatosParaFormularioCita(model, usuarioLogueado, esPaciente);
            return "admin/cita";
        }

        // Si no se seleccionó estado desde el formulario, asignar PENDIENTE
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCita.PENDIENTE);
        }

        // Relacionar entidades
        Usuario usuario = usuarioService.obtenerUsuarioPorId(cita.getUsuario().getId_usuario())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el usuario.", "/admin/cita/nuevo"));
        Mascota mascota = mascotaService.obtenerMascotaPorId(cita.getMascota().getId_mascota())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la mascota.", "/admin/cita/nuevo"));
        Veterinaria veterinaria = veterinariaService.obtenerVeterinariaPorId(cita.getVeterinaria().getId_vet())
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la veterinaria.", "/admin/cita/nuevo"));

        cita.setUsuario(usuario);
        cita.setMascota(mascota);
        cita.setVeterinaria(veterinaria);

        // Veterinario puede ser null
        if (cita.getVeterinario() != null && cita.getVeterinario().getId_veterinario() != null) {
            Veterinario veterinario = veterinarioService.obtenerVeterinarioPorId(cita.getVeterinario().getId_veterinario())
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la veterinario.", "/admin/cita/nuevo"));;
            cita.setVeterinario(veterinario);
        } else {
            cita.setVeterinario(null);
        }

        // Detectar si es nueva o edición antes de guardar
        boolean esNueva = (cita.getId_cita() == null || !citaService.existePorId(cita.getId_cita()));

        try {
            citaService.guardarCita(cita);
        } catch (ObjectOptimisticLockingFailureException e) {
            String mensaje = "La cita fue modificada por otro usuario. Estás viendo la versión más reciente.";
            redirectAttributes.addFlashAttribute("conflicto", mensaje);
            return "redirect:/admin/cita/editar?id=" + cita.getId_cita();
        }

        // Mensaje de éxito y redirección
        String mensaje = esNueva
                ? "¡Cita registrada exitosamente!"
                : "¡Cita modificada exitosamente!";
        redirectAttributes.addFlashAttribute("exito", mensaje);
        return "redirect:/admin/cita/nuevo";
    }

    // Elimina una cita por ID
    @GetMapping("/eliminar")
    public String eliminarCita(@RequestParam Long id) {
        Cita cita = citaService.obtenerCitaPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la cita.", "/admin/cita/nuevo"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente) {
            if (!cita.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario()) ||
                    !(cita.getEstado() == EstadoCita.PENDIENTE || cita.getEstado() == EstadoCita.CONFIRMADA)) {
                String mensaje = "No tienes permiso para eliminar esta cita.";
                return "redirect:/admin/cita/nuevo?denegado=" + java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
            }
        }

        citaService.eliminarCita(id);
        return "redirect:/admin/cita/nuevo";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar")
    public String mostrarFormularioEditar(@RequestParam Long id,
                                          @RequestParam(required = false) String conflicto,
                                          Model model) {
        Cita cita = citaService.obtenerCitaPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la cita.", "/admin/cita/nuevo"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogueado = usuarioService.buscarPorUsername(auth.getName());

        boolean esPaciente = SecurityUtils.esPacienteSinSerAdmin(auth);

        if (esPaciente) {
            // Validar que la cita le pertenezca y esté en estado editable
            if (!cita.getUsuario().getId_usuario().equals(usuarioLogueado.getId_usuario()) ||
                    !(cita.getEstado() == EstadoCita.PENDIENTE || cita.getEstado() == EstadoCita.CONFIRMADA)) {
                String mensaje = "No tienes permiso para editar esta cita.";
                return "redirect:/admin/cita/nuevo?denegado=" +
                        java.net.URLEncoder.encode(mensaje, java.nio.charset.StandardCharsets.UTF_8);
            }
        }

        model.addAttribute("cita", cita);
        cargarDatosParaFormularioCita(model, usuarioLogueado, esPaciente);

        if (conflicto != null) {
            model.addAttribute("error", conflicto);
        }

        return "admin/cita";
    }

    @GetMapping("/actualizar-estados")
    public String actualizarEstadosCita() {
        citaService.actualizarEstadosCita();
        return "redirect:/admin/cita/nuevo";
    }

    //Carga de datos con validación si es paciente o admin/vet
    private void cargarDatosParaFormularioCita(Model model, Usuario usuarioLogueado, boolean esPaciente) {
        if (esPaciente) {
            model.addAttribute("usuarios", List.of(usuarioLogueado));
            model.addAttribute("mascotas", mascotaService.listarMascotasPorUsuario(usuarioLogueado.getId_usuario()));
            model.addAttribute("citas", citaService.listarCitasPorUsuario(usuarioLogueado.getId_usuario()));
        } else {
            model.addAttribute("usuarios", usuarioService.listarUsuariosConRol(RoleName.ROLE_PACIENTE));
            model.addAttribute("mascotas", mascotaService.listarMascotas());
            model.addAttribute("citas", citaService.listarCitas());
        }

        model.addAttribute("veterinarias", veterinariaService.listarVeterinarias());
        model.addAttribute("veterinarios", veterinarioService.listarVeterinarios());
        model.addAttribute("estados", EstadoCita.values());
    }


    // Captura excepción de version
    @ExceptionHandler(OptimisticLockException.class)
    public String manejarConflictoOptimista(Model model) {
        model.addAttribute("error", "La cita fue modificada por otro usuario. Por favor, actualiza y vuelve a intentar.");
        return "admin/cita";
    }
}