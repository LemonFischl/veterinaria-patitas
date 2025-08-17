package com.miempresa.proyectofinal.model;

import com.miempresa.proyectofinal.validator.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario") // NOMBRE DE TABLA ACTUALIZADO A 'usuario'
@UniqueEmail // validador
public class Usuario implements UserDetails { // UserDetails para representar un usuario autenticado dentro de la aplicación
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    @Column(unique = true, nullable = false) // Agregado nullable = false
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
    )
    @Column(nullable = false) // Agregado nullable = false
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    @Column(nullable = false) // Agregado nullable = false
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Column(nullable = false) // Agregado nullable = false
    private String email;

    @Digits(integer = 9, fraction = 0, message = "El celular debe contener exactamente 9 dígitos")
    private String celular;

    // --- Relación con MASCOTAS
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas;

    // Relación muchos a muchos con roles (Hara que se cre una tabla intermedia para la relación)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // ***** IMPORTANTE: Esto asume que tu clase Role implementará GrantedAuthority *****
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles; // Devuelve directamente el Set<Role> si Role implementa GrantedAuthority
    }

    //Indica si la cuenta no ha expirado.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Indica si la cuenta no está bloqueada.
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Indica si las credenciales (como contraseña) del usuario están vigentes.
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Indica si el usuario está habilitado para iniciar sesión.
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Constructores

    public Usuario(){}

    // Constructor
    public Usuario(Long id_usuario, String username, String nombre, String password, String email, String celular, List<Mascota> mascotas, Set<Role> roles) {
        this.id_usuario = id_usuario;
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.email = email;
        this.celular = celular;
        this.mascotas = mascotas; // Adaptado
        this.roles = roles;
    }

    //Getters and Setter
    public Long getId_usuario() { return id_usuario; }
    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }

    @Override
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    @Override
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    // --- CAMBIO CLAVE: Getters y Setters
    public List<Mascota> getMascotas() { return mascotas; }
    public void setMascotas(List<Mascota> mascotas) { this.mascotas = mascotas; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

}
