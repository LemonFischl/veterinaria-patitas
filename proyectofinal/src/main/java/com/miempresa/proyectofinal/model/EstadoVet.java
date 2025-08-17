package com.miempresa.proyectofinal.model;

public enum EstadoVet {
    ACTIVO("bg-success"),
    INACTIVO("bg-danger");

    private final String cssClass;

    EstadoVet(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }
}
