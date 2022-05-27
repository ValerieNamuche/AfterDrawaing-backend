package com.afterdrawing.backendapi.core.service;

import com.afterdrawing.backendapi.core.entity.Wireframe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface WireframeService {
    Page<Wireframe> getAllWireframes(Pageable pageable);

    Wireframe getWireframeById(Long wireframe);




    //Wireframe updateWireframe(Long wireframeId, Wireframe wireframeRequest, Long interfaceId);

    //Wireframe saveWireframe(Wireframe wireframe, Long interfaceId);

    ResponseEntity<?> deleteWireframe(Long wireframeId);

    List<String> getClasses(String projectId, String modelId, byte[] content) throws IOException;

    List<Float> getX1();
    List<Float> getY1();
    List<Float> getX2();
    List<Float> getY2();

    List<String> getWireframeCode() throws IOException;

    byte[] getImage();

}
