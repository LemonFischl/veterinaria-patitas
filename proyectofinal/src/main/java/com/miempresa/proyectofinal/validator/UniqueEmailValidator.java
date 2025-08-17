package com.miempresa.proyectofinal.validator;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.repository.UsuarioRegisterRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, Usuario> {

    private UsuarioRegisterRepository usuarioRegisterRepository;

    public UniqueEmailValidator() {
        // Constructor por defecto requerido por Hibernate Validator
    }

    @Autowired
    public void setUsuarioRepository(UsuarioRegisterRepository usuarioRegisterRepository) {
        this.usuarioRegisterRepository = usuarioRegisterRepository;
    }

    @Override
    public boolean isValid(Usuario usuario, ConstraintValidatorContext context) {
        if (usuario == null || usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            return true; // Se asume que @NotBlank/@Email se encargan de la obligatoriedad y formato.
        }

        String email = usuario.getEmail();
        Long userId = usuario.getId_usuario();

        if (usuarioRegisterRepository == null) { // Asegurarse que el repositorio está inyectado
            return true; // O lanzar una excepción, pero en validación es mejor ser permisivo si el validador no está listo.
        }

        java.util.Optional<Usuario> usuarioExistente = usuarioRegisterRepository.findByEmail(email);

        if (usuarioExistente.isPresent()) {
            // Si el email existe, es válido solo si pertenece al usuario que se está actualizando.
            boolean esMismoUsuario = userId != null && usuarioExistente.get().getId_usuario().equals(userId);
            if (!esMismoUsuario) {
                context.disableDefaultConstraintViolation(); // Deshabilitar el mensaje de error global por defecto
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()) // Usar el mensaje de la anotación
                        .addPropertyNode("email") // Asociar el error con el campo 'email'
                        .addConstraintViolation(); // Construir la violación
            }
            return esMismoUsuario;
        }

        return true; // El email no existe, por lo tanto es único.

    }
}
