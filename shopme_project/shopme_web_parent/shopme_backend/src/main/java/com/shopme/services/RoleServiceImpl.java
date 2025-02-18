package com.shopme.services;

import org.springframework.stereotype.Service;

import com.shopme.entities.Role;
import com.shopme.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Iterable<Role> findAll() {
        return this.roleRepository.findAll();
    }

}
