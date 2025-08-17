package com.miempresa.proyectofinal.model;
import com.miempresa.proyectofinal.validator.DiaAtencionValido;
import com.miempresa.proyectofinal.validator.HoraDentroDeHorario;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
//@DiaAtencionValido
//@HoraDentroDeHorario
@Table(name = "cita")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cita;

    @NotNull(message = "La fecha es obligatoria")
    //@FutureOrPresent(message = "La fecha no puede estar en el pasado") para cuando el usuario lo registre
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

    @Size(max = 255, message = "Los comentarios no deben superar los 255 caracteres")
    private String comentarios;

    @Min(0)
    @Version
    private int version;

    @Lob
    @Size(max = 2000, message = "El historial no debe exceder los 2000 caracteres")
    private String historialVeterinario;

    @NotNull(message = "El estado de la cita es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "id_vet", nullable = false)
    private Veterinaria veterinaria;

    @ManyToOne(optional = true) // puede ser null al principio
    @JoinColumn(name = "id_veterinario")
    private Veterinario veterinario;

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Long getId_cita() {
        return id_cita;
    }

    public void setId_cita(Long id_cita) {
        this.id_cita = id_cita;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Veterinaria getVeterinaria() {
        return veterinaria;
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getHistorialVeterinario() {
        return historialVeterinario;
    }

    public void setHistorialVeterinario(String historialVeterinario) {
        this.historialVeterinario = historialVeterinario;
    }

    //Metodo para obtener la clase CSS de estado
    public String getEstadoCssClass() {
        return estado != null ? estado.getCssClass() : "bg-light text-dark";
    }
}