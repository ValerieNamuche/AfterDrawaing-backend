package com.afterdrawing.backendapi.core.entity;

import lombok.Data;

@Data
public class JwtRequest {

    private String username;
    private String password;
}
