package com.miempresa.proyectofinal.model;

public enum EstadoCita {
    PENDIENTE("bg-secondary"), // Cita registrada, pero no confirmada (usuario recién la creó)
    CONFIRMADA("bg-primary"),  // Cita aprobada por la veterinaria o confirmada por el sistema
    CANCELADA("bg-dark"),      // Cancelada por el usuario o administrador
    ATENDIDA("bg-success");    // El paciente asistió y fue atendido

    private final String cssClass;

    EstadoCita(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
