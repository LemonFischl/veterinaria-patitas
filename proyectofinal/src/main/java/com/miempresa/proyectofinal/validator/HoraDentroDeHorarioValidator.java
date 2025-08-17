package com.miempresa.proyectofinal.validator;

import com.miempresa.proyectofinal.model.Cita;
import com.miempresa.proyectofinal.repository.CitaRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.miempresa.proyectofinal.model.Veterinaria;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class HoraDentroDeHorarioValidator implements ConstraintValidator<HoraDentroDeHorario, Cita> {

    @Override
    public boolean isValid(Cita cita, ConstraintValidatorContext context) {
        if (cita.getVeterinaria() == null || cita.getHora() == null) return true;

        LocalTime hora = cita.getHora();
        LocalTime inicio = cita.getVeterinaria().getHoraInicio();
        LocalTime fin = cita.getVeterinaria().getHoraFin();

        if (inicio == null || fin == null) {
            System.out.println("Advertencia: Horario de atención no definido para la veterinaria: " + cita.getVeterinaria().getNombre());
            return true;
        }

        if (hora.isBefore(inicio) || hora.isAfter(fin)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("hora")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

