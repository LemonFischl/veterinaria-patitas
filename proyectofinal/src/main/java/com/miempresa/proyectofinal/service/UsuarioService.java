package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.repository.UsuarioRegisterRepository;
import com.miempresa.proyectofinal.model.RoleName;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class UsuarioService {
    private final UsuarioRegisterRepository usuarioRegisterRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRegisterRepository usuarioRegisterRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRegisterRepository = usuarioRegisterRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void guardarUsuario(Usuario usuario) {
        try {
            // Log para debug
            System.out.println("=== INICIANDO GUARDADO DE USUARIO ===");
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Roles: " + usuario.getRoles().size());

            // Verificar si el email ya existe
            if (usuario.getId_usuario() == null && existeEmail(usuario.getEmail())) {
                throw new RuntimeException("El email ya está registrado");
            }

            // Verificar si el username ya existe
            if (usuario.getId_usuario() == null && usuarioRegisterRepository.findByUsername(usuario.getUsername()).isPresent()) {
                throw new RuntimeException("El nombre de usuario ya está registrado");
            }

            // Codificar contraseña solo si es nueva o no está codificada
            if (usuario.getId_usuario() == null || !usuario.getPassword().startsWith("$2a$")) {
                String passwordOriginal = usuario.getPassword();
                String passwordCodificada = passwordEncoder.encode(passwordOriginal);
                usuario.setPassword(passwordCodificada);
                System.out.println("Contraseña codificada exitosamente");
            }

            // Guardar usuario
            Usuario usuarioGuardado = usuarioRegisterRepository.save(usuario);
            System.out.println("Usuario guardado con ID: " + usuarioGuardado.getId_usuario());
            System.out.println("=== GUARDADO EXITOSO ===");

        } catch (Exception e) {
            System.err.println("=== ERROR AL GUARDAR USUARIO ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRegisterRepository.findAll(); }

    public boolean existeEmail(String email) {
        return usuarioRegisterRepository.findByEmail(email).isPresent();
    }

    public void eliminarUsuario(Long id) {
        usuarioRegisterRepository.deleteById(id);
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRegisterRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRegisterRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public List<Usuario> listarUsuariosConRol(RoleName rol) {
        return usuarioRegisterRepository.findUsuariosByRol(rol);
    }
}
