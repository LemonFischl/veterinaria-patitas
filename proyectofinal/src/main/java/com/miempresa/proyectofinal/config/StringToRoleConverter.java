package com.miempresa.proyectofinal.config;

import org.springframework.core.convert.converter.Converter;
import com.miempresa.proyectofinal.model.Role;
import com.miempresa.proyectofinal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role convert(String id) {
        return roleRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
