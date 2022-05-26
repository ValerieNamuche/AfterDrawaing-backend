package com.afterdrawing.backendapi.core.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(max = 25)
    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Size(max = 25)
    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Size(max = 25)
    @Column(name = "lastName",nullable = false)
    private String lastName;

    @Size(max = 50)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 25)
    @Lob
    @Column(name = "password", nullable = false)
    private String password;


    private Boolean using2FA;

    private String secretKey;

    private Boolean enabled;
}
