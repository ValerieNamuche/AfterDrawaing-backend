package com.afterdrawing.backendapi.resource;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Size;

@Data
public class SaveWireframeResource {
    @Size(max = 15)
    @Column(name = "wireFrameName", nullable = false, unique = true)
    private String wireFrameName;

    //siguiente desarrollador trabaje la implementación de ruta
    @Size(max = 80)
    @Column(name = "route", nullable = false, unique = true)
    private String route;

}
