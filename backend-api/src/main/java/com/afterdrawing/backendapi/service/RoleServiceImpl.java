package com.afterdrawing.backendapi.service;

import com.afterdrawing.backendapi.core.entity.Authority;
import com.afterdrawing.backendapi.core.repository.RoleRepository;
import com.afterdrawing.backendapi.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Authority createNewRole (Authority authority){
        return roleRepository.save(authority);
    }

}
