package com.miempresa.proyectofinal.security;

import com.miempresa.proyectofinal.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeHttpRequests(authz -> authz
                        // Recursos públicos
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/IMG/**").permitAll()

                        //Rutas para mascota
                        .requestMatchers("/paciente/mascota/nuevo", "/paciente/mascota/guardar").hasAnyRole("ADMIN", "PACIENTE", "VET")

                        //Rutas para citas
                        .requestMatchers("/admin/cita/**").hasAnyRole("PACIENTE", "VET", "ADMIN")

                        //Rutas para veterinarias
                        .requestMatchers("/vet/veterinaria/nuevo", "/vet/veterinaria/guardar", "/vet/veterinaria/editar/**", "/vet/veterinaria/eliminar/**").hasRole("ADMIN")
                        .requestMatchers("/vet/veterinaria/listar").hasAnyRole("ADMIN", "VET")

                        //Rutas para veterinarios
                        .requestMatchers("/vet/veterinario/**").hasAnyRole("ADMIN", "VET")

                        // Rutas para administrador
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Rutas para vets
                        .requestMatchers("/vet/**").hasAnyRole("ADMIN", "VET")

                        // Rutas para pacientes
                        .requestMatchers("/paciente/**").hasAnyRole("ADMIN", "PACIENTE")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}
