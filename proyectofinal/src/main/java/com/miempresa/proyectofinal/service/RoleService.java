package com.miempresa.proyectofinal.service;

import com.miempresa.proyectofinal.model.Role;
import com.miempresa.proyectofinal.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> listarRoles(){
        return roleRepository.findAll();
    }
}
