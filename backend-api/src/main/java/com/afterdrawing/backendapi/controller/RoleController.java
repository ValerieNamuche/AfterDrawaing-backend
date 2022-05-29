package com.afterdrawing.backendapi.controller;


import com.afterdrawing.backendapi.core.entity.Authority;
import com.afterdrawing.backendapi.core.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/createNewRole")
    public Authority createNewRole(@RequestBody Authority authority){
        return roleService.createNewRole(authority);
    }

}
