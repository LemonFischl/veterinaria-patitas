package com.miempresa.proyectofinal.validator;

import com.miempresa.proyectofinal.model.Cita;
import com.miempresa.proyectofinal.model.DiasAtencion;
import com.miempresa.proyectofinal.service.VeterinariaService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiaAtencionValidator implements ConstraintValidator<DiaAtencionValido, Cita> {

    @Autowired
    private VeterinariaService veterinariaService;

    private DiasAtencion convertirADiasAtencion(java.time.DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiasAtencion.LUNES;
            case TUESDAY -> DiasAtencion.MARTES;
            case WEDNESDAY -> DiasAtencion.MIERCOLES;
            case THURSDAY -> DiasAtencion.JUEVES;
            case FRIDAY -> DiasAtencion.VIERNES;
            case SATURDAY -> DiasAtencion.SABADO;
            case SUNDAY -> DiasAtencion.DOMINGO;
        };
    }

    @Override
    public boolean isValid(Cita cita, ConstraintValidatorContext context) {
        if (cita.getVeterinaria() == null || cita.getVeterinaria().getId_vet() == null || cita.getFecha() == null) {
            return true; // No valida si no hay veterinaria o fecha
        }

        var veterinariaCompleta = veterinariaService.obtenerVeterinariaPorId(cita.getVeterinaria().getId_vet());
        if (veterinariaCompleta == null || veterinariaCompleta.getDiasDeAtencion() == null) {
            return true;
        }

        DiasAtencion diaEnum = convertirADiasAtencion(cita.getFecha().getDayOfWeek());

        if (!veterinariaCompleta.getDiasDeAtencion().contains(diaEnum)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("fecha")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}