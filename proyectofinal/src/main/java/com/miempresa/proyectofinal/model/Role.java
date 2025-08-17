package com.miempresa.proyectofinal.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority; // Import GrantedAuthority

@Entity
@Table(name="roles")
public class Role implements GrantedAuthority { // GrantedAuthority
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private RoleName name;

    public Role(){ }

    public Role(RoleName name) {
        this.name = name;
    }

    // Required "ROLE_ADMIN").
    @Override
    public String getAuthority() {
        return name.name();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }
}