package com.paglaai.estimaai.service;

import com.paglaai.estimaai.repository.entity.RoleEntity;
import com.paglaai.estimaai.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity getRoleByName(String roleName){
        return roleRepository.findByName(roleName);
    }
    public RoleEntity checkRoleExist(){
        RoleEntity role = new RoleEntity();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

}
