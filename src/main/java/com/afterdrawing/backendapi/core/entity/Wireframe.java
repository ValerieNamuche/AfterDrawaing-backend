package com.afterdrawing.backendapi.core.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "wireframe")
@Data
public class Wireframe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 15)
    @Column(name = "wireFrameName", nullable = false, unique = true)
    private String wireFrameName;

    //siguiente desarrollador trabaje la implementación de ruta
    @Size(max = 80)
    @Column(name = "route", nullable = false, unique = true)
    private String route;

    @OneToOne
    @JoinColumn(name = "interface_id", updatable = false, nullable = false)
    private Interface anInterface;



}
