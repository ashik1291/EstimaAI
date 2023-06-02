package com.paglaai.estimaai.service;

import com.paglaai.estimaai.repository.entity.Role;
import com.paglaai.estimaai.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getRoleByName(String roleName){
        return roleRepository.findByName(roleName);
    }
    public Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

}
