package com.afterdrawing.backendapi.resource;



import lombok.Data;

import javax.persistence.Column;

import javax.validation.constraints.Size;
@Data
public class SaveProjectResource {

    @Size(max = 15)
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 15)
    @Column(name = "description", nullable = false)
    private String description;


}
