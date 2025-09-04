package com.miempresa.proyectofinal.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "veterinario")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_veterinario;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max= 50, message="El nombre no puede exceder 50 caracteres")
    private String nombre;

    @Pattern(regexp = "\\d{9}", message = "El número de celular debe tener 9 dígitos")
    private String celular;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message="Formato de correo electrónico inválido")
    private String correo;

    @NotBlank(message = "El horario es obligatorio")
    private String horarioAtencion;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaContratacion;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "id_vet")
    private Veterinaria veterinaria;

    @OneToMany(mappedBy = "veterinario")
    private List<Cita> citas;

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Long getId_veterinario() {
        return id_veterinario;
    }

    public void setId_veterinario(Long id_veterinario) {
        this.id_veterinario = id_veterinario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getHorarioAtencion() {
        return horarioAtencion;
    }

    public void setHorarioAtencion(String horarioAtencion) {
        this.horarioAtencion = horarioAtencion;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public Veterinaria getVeterinaria() {
        return veterinaria;
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
    }
}