package com.afterdrawing.backendapi.core.service;

import com.afterdrawing.backendapi.core.entity.Wireframe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface WireframeService {
    Page<Wireframe> getAllWireframes(Pageable pageable);

    Wireframe getWireframeById(Long wireframe,Long interfaceId);




    Wireframe updateWireframe(Long wireframeId, Wireframe wireframeRequest, Long interfaceId);

    Wireframe saveWireframe(Wireframe wireframe, Long interfaceId);

    ResponseEntity<?> deleteWireframe(Long wireframeId);
}
