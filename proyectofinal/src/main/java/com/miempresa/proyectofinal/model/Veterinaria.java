package com.miempresa.proyectofinal.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import org.hibernate.validator.constraints.URL;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "veterinaria")
public class Veterinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_vet;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(unique = true, nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe ser un formato de correo electrónico válido")
    @Column(length = 150)
    private String correoElectronico;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos numéricos")
    @Column(nullable = false, length = 15)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(nullable = false, length = 255)
    private String direccion;

    @URL(message = "Debe ser un formato de URL válido")
    @Column(length = 255)
    private String sitioWeb;

    @Column(length = 1000)
    private String descripcion;

    @URL(message = "Debe ser una URL válida")
    @Column(length = 500)
    private String imagenUrl;

    @NotNull(message = "La hora de inicio no puede ser nula")
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin no puede ser nula")
    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @OneToMany(mappedBy = "veterinaria")
    private List<Cita> citas;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVet estadoVet;

    @ElementCollection(targetClass = ServicioGeneral.class)
    @CollectionTable(
            name = "veterinaria_servicios_generales",
            joinColumns = @JoinColumn(name = "veterinaria_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "servicio_general")
    @NotEmpty(message = "Debe seleccionar al menos un servicio general")
    private Set<ServicioGeneral> serviciosGenerales = new HashSet<>();

    @ElementCollection(targetClass = DiasAtencion.class)
    @CollectionTable(
            name = "veterinaria_dias_atencion",
            joinColumns = @JoinColumn(name = "veterinaria_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_atencion")
    @NotEmpty(message = "Debe seleccionar al menos un día de atención")
    private Set<DiasAtencion> diasDeAtencion = new HashSet<>();

    public Veterinaria() {
    }

    // --- Getters y Setters ---

    public Long getId_vet() {
        return id_vet;
    }

    public void setId_vet(Long id_vet) {
        this.id_vet = id_vet;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
    }

    public EstadoVet getEstadoVet() {
        return estadoVet;
    }

    public void setEstadoVet(EstadoVet estadoVet) {
        this.estadoVet = estadoVet;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Set<ServicioGeneral> getServiciosGenerales() {
        return serviciosGenerales;
    }

    public void setServiciosGenerales(Set<ServicioGeneral> serviciosGenerales) {
        this.serviciosGenerales = serviciosGenerales;
    }

    public Set<DiasAtencion> getDiasDeAtencion() {
        return diasDeAtencion;
    }

    public void setDiasDeAtencion(Set<DiasAtencion> diasDeAtencion) {
        this.diasDeAtencion = diasDeAtencion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

        //Metodo para obtener la clase CSS de estado
    public String getEstadoVetCssClass() {
        return estadoVet != null ? estadoVet.getCssClass() : "bg-light text-dark";
    }

    // Devuelve los días de atención en orden de LUNES a DOMINGO
    public List<DiasAtencion> getDiasDeAtencionOrdenados() {
        List<DiasAtencion> ordenNatural = List.of(DiasAtencion.values());
        return ordenNatural.stream()
                .filter(diasDeAtencion::contains)
                .toList();
    }
}