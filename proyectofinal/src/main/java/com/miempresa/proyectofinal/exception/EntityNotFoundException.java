package com.miempresa.proyectofinal.exception;

public class EntityNotFoundException extends RuntimeException {
    private final String viewName; // aquí se guarda la vista a devolver

    public EntityNotFoundException(String message, String viewName) {
        super(message);
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}