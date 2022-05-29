package com.afterdrawing.backendapi.resource.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class AuthenticationResource {
    private String authenticationToken;
   // private Instant expiresAt;
    private String email;
    private String password;
    //private Boolean using2fa;
}
