package com.afterdrawing.backendapi.service;

import com.afterdrawing.backendapi.core.entity.Interface;
import com.afterdrawing.backendapi.core.entity.User;
import com.afterdrawing.backendapi.core.entity.Wireframe;
import com.afterdrawing.backendapi.core.repository.InterfaceRepository;
import com.afterdrawing.backendapi.core.repository.WireframeRepository;
import com.afterdrawing.backendapi.core.service.WireframeService;
import com.afterdrawing.backendapi.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class WireframeServiceImpl implements WireframeService {

    @Autowired
    WireframeRepository wireframeRepository;
    @Autowired
    InterfaceRepository interfaceRepository;

    @Override
    public Page<Wireframe> getAllWireframes(Pageable pageable) {
        return wireframeRepository.findAll(pageable);
    }

    @Override
    public Wireframe getWireframeById(Long wireframeId,Long interfaceId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        return wireframe;
    }

    @Override
    public Wireframe updateWireframe(Long wireframeId, Wireframe WireframeRequest, Long interfaceId) {
        Interface anInterface= interfaceRepository.findById(interfaceId).orElseThrow(()-> new ResourceNotFoundException("Interface","Id", interfaceId));
        if (anInterface ==null){
            throw new ResourceNotFoundException("Invalid Interface, a Interface no exists with that value" );
        }
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        wireframe.setWireFrameName(WireframeRequest.getWireFrameName());
        wireframe.setRoute(WireframeRequest.getRoute());

        return wireframeRepository.save(wireframe);
    }

    @Override
    public Wireframe saveWireframe(Wireframe wireframe,Long interfaceId) {
        Interface anInterface= interfaceRepository.findById(interfaceId).orElseThrow(()-> new ResourceNotFoundException("Interface","Id", interfaceId));
        if(wireframe.getAnInterface()== anInterface){
            throw new ResourceNotFoundException("Invalid wireframe, a wireframe already exists with that value" );
        }

        wireframe.setAnInterface(anInterface);
        return wireframeRepository.save(wireframe);
    }

    @Override
    public ResponseEntity<?> deleteWireframe(Long wireframeId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        wireframeRepository.delete(wireframe);
        return ResponseEntity.ok().build();
    }
}
