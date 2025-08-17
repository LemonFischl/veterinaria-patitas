package com.miempresa.proyectofinal.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = HoraDentroDeHorarioValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface HoraDentroDeHorario {
    String message() default "La hora está fuera del horario de atención de la veterinaria.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
