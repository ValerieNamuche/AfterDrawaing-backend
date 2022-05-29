package com.afterdrawing.backendapi.resource;

import com.afterdrawing.backendapi.core.entity.Project;
import com.afterdrawing.backendapi.core.entity.User;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Data
public class SaveWireframeResource {
    @Size(max = 50)
    @Column(name = "name", nullable = false, unique = false)
    private String name;

    @ManyToOne
    @JoinColumn(columnDefinition="integer", name = "project_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne
    @JoinColumn(columnDefinition="integer", name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    //siguiente desarrollador trabaje la implementación de ruta
    /*
    @Size(max = 80)
    @Column(name = "route", nullable = false, unique = true)
    private String route;

     */

}
