package com.miempresa.proyectofinal.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtils {

    public static boolean tieneRol(Authentication auth, String rol) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(rol));
    }

    public static boolean esPacienteSinSerAdmin(Authentication auth) {
        return tieneRol(auth, "ROLE_PACIENTE") && !tieneRol(auth, "ROLE_ADMIN");
    }

}