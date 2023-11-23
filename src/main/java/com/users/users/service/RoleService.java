package com.users.users.service;

import com.users.users.dto.RoleDTO;
import com.users.users.model.Role;
import com.users.users.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<RoleDTO> findAll() {
        return createListRoleDTO(roleRepository.findAll());
    }

    public void add(Role role) {
        roleRepository.save(role);
    }

    public void deleteById(Integer id) {
        roleRepository.deleteById(id);
    }

    public List<RoleDTO> getByName(String name) {
        return createListRoleDTO(roleRepository.getByName(name));
    }


    private List<RoleDTO> createListRoleDTO(List<Role> roles){
        return roles.stream()
                .map(role -> new RoleDTO(role.getId(), new HashMap<>(Map.of(role.getName(), role.getUsers()))))
                .collect(Collectors.toList());
    }
}
