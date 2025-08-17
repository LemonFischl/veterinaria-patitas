package com.miempresa.proyectofinal.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "El correo electrónico ya está registrado.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}