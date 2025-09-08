package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Usuario;
import com.miempresa.proyectofinal.repository.CitaRepository;
import com.miempresa.proyectofinal.repository.UsuarioRegisterRepository;
import com.miempresa.proyectofinal.model.RoleName;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class UsuarioService {
    private final UsuarioRegisterRepository usuarioRegisterRepository;
    private final PasswordEncoder passwordEncoder;
    private final CitaRepository citaRepository;

    @Autowired
    public UsuarioService(UsuarioRegisterRepository usuarioRegisterRepository, PasswordEncoder passwordEncoder, CitaRepository citaRepository) {
        this.usuarioRegisterRepository = usuarioRegisterRepository;
        this.passwordEncoder = passwordEncoder;
        this.citaRepository = citaRepository;
    }

    @Transactional
    public void guardarUsuario(Usuario usuario) {
        try {
            // Log para debug
            System.out.println("=== INICIANDO GUARDADO DE USUARIO ===");
            System.out.println("Username: " + usuario.getUsername());
            System.out.println("Email: " + usuario.getEmail());
            System.out.println("Roles: " + usuario.getRoles().size());

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

    public boolean existeEmail(Usuario usuario) {
        Optional<Usuario> existente = usuarioRegisterRepository.findByEmail(usuario.getEmail());
        return existente.isPresent() &&
                !existente.get().getId_usuario().equals(usuario.getId_usuario());
    }

    public boolean existeUsername(Usuario usuario) {
        Optional<Usuario> existente = usuarioRegisterRepository.findByUsername(usuario.getUsername());
        return existente.isPresent() &&
                !existente.get().getId_usuario().equals(usuario.getId_usuario());
    }
    public void eliminarUsuario(Long id) {
        boolean tieneCitas = citaRepository.existsByUsuario(id);
        if (tieneCitas) {
            throw new IllegalStateException("No se puede eliminar el usuario con citas asociadas.");
        }
        usuarioRegisterRepository.deleteById(id);
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRegisterRepository.findById(id);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioRegisterRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public List<Usuario> listarUsuariosConRol(RoleName rol) {
        return usuarioRegisterRepository.findUsuariosByRol(rol);
    }
}
