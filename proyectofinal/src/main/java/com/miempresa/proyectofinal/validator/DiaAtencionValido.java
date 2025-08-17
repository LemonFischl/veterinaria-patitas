package com.miempresa.proyectofinal.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DiaAtencionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DiaAtencionValido {
    String message() default "La veterinaria no atiende ese día";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}