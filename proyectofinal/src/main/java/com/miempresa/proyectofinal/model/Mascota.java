package com.miempresa.proyectofinal.model;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "mascota")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mascota;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de mascota es obligatorio")
    @Size(max = 30, message = "El tipo no puede exceder 30 caracteres")
    private String tipo;

    @Size(max = 50, message = "La raza no puede exceder 50 caracteres")
    private String raza;

    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Lob
    @Size(max = 255, message = "Las observaciones no pueden exceder 255 caracteres")
    private String observacionesMedicas;

    @NotNull(message = "El género es obligatorio")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Transient
    private boolean alerta;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "mascota")
    private List<Cita> citas;

    //Getters y Setters

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(Long id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }
    public String getObservacionesMedicas() {
        return observacionesMedicas;
    }

    public void setObservacionesMedicas(String observacionesMedicas) {
        this.observacionesMedicas = observacionesMedicas;
    }

    public boolean isAlerta() {
        return alerta;
    }

    public void setAlerta(boolean alerta) {
        this.alerta = alerta;
    }
}