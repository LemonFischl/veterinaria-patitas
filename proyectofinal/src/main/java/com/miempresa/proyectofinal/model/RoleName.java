package com.miempresa.proyectofinal.model;

public enum RoleName {
    ROLE_ADMIN,      // Para los administradores de la clínica (acceso total)
    ROLE_VET, // Para el personal veterinario (gestión de citas, pacientes, etc.)
    ROLE_PACIENTE     // Para los dueños de mascotas (agendar citas, ver historial de sus mascotas)
}