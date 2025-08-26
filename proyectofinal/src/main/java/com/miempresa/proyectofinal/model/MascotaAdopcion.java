package com.miempresa.proyectofinal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "mascotas_adopcion")
public class MascotaAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de mascota es obligatorio (perro, gato, etc.)")
    @Size(max = 30, message = "El tipo no puede exceder 30 caracteres")
    private String tipo;

    @Size(max = 50, message = "La raza no puede exceder 50 caracteres")
    private String raza;

    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El género es obligatorio")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Lob
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoAdopcion estado;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaIngreso; // al refugio

    private Boolean esterilizado;

    private Boolean vacunado;

    private Boolean desparasitado;

    private String urlFoto;

//    @ManyToOne
//    @JoinColumn(name = "id_refugio")
//    private Refugio refugio;

    // Constructores

    public MascotaAdopcion() {
    }

    // Getters y Setters

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDesparasitado() {
        return desparasitado;
    }

    public void setDesparasitado(Boolean desparasitado) {
        this.desparasitado = desparasitado;
    }

    public EstadoAdopcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAdopcion estado) {
        this.estado = estado;
    }

    public Boolean getEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(Boolean esterilizado) {
        this.esterilizado = esterilizado;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Boolean getVacunado() {
        return vacunado;
    }

    public void setVacunado(Boolean vacunado) {
        this.vacunado = vacunado;
    }
}
