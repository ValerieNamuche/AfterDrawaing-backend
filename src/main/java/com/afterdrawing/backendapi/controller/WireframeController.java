package com.afterdrawing.backendapi.controller;

import com.afterdrawing.backendapi.core.entity.Wireframe;
import com.afterdrawing.backendapi.core.entity.Wireframe;
import com.afterdrawing.backendapi.core.entity.Wireframe;
import com.afterdrawing.backendapi.core.service.WireframeService;
import com.afterdrawing.backendapi.resource.*;
import com.afterdrawing.backendapi.resource.SaveWireframeResource;
import com.afterdrawing.backendapi.resource.WireframeResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WireframeController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private WireframeService wireframeService;


    @Operation(summary = "Get wireframes", description = "Get All wireframes by Pages", tags = { "wireframes" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All wireframes returned", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/wireframes")
    public Page<WireframeResource> getAllWireframes(Pageable pageable) {
        Page<Wireframe> wireframePage = wireframeService.getAllWireframes(pageable);
        List<WireframeResource> resources = wireframePage.getContent().stream().map(this::convertToResource).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, resources.size());
    }


    @Operation(summary = "Get Wireframe by Id", description = "Get a Wireframe by specifying Id", tags = { "wireframes" })
    @GetMapping("/interfaces/{interfaceId}/wireframes/{wireframeId}")
    public WireframeResource getWireframeByIdAndInterfaceId(
            @Parameter(description="Wireframe Id")
            @PathVariable(name = "wireframeId") Long wireframeId,
            @Parameter(description="Interface Id")
            @PathVariable(name = "interfaceId") Long interfaceId) {
        return convertToResource(wireframeService.getWireframeById(wireframeId,interfaceId));
    }


    //@Operation(security={ @SecurityRequirement(name="Authorization") })
    @Operation(summary = "Create Wireframe ", description = "Create a Wireframe", tags = { "wireframes" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wireframe Created and returned", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("interfaces/{interfaceId}/wireframes")
    public WireframeResource createWireframe(@RequestBody SaveWireframeResource resource, @Parameter(description="Wireframe Id")
    @PathVariable(name = "interfaceId") Long interfaceId)  {
        Wireframe wireframe = convertToEntity(resource);
        return convertToResource(wireframeService.saveWireframe(wireframe,interfaceId));
    }
    @Operation(summary = "Update Wireframe ", description = "Update a Wireframe", tags = { "wireframes" })
    @PutMapping("/interfaces/{interfaceId}/wireframes/{wireframeId}")
    public WireframeResource updateWireframe(@PathVariable(name = "wireframeId") Long wireframeId,
                                             @Valid @RequestBody SaveWireframeResource resource,
                                             @PathVariable(name = "interfaceId") Long interfaceId   ) {
        Wireframe wireframe = convertToEntity(resource);
        return convertToResource(wireframeService.updateWireframe(wireframeId, wireframe,interfaceId));
    }
    @Operation( summary = "Delete Wireframe ", description = "Delete a Wireframe", tags = { "wireframes" })
    @DeleteMapping("/wireframes/{wireframeId}")
    public ResponseEntity<?> deleteWireframe(@PathVariable(name = "wireframeId") Long wireframeId) {
        return wireframeService.deleteWireframe(wireframeId);
    }
    
    
    
    // Auto Mapper
    private Wireframe convertToEntity(SaveWireframeResource resource) {
        return mapper.map(resource, Wireframe.class);
    }

    private WireframeResource convertToResource(Wireframe entity) {
        return mapper.map(entity, WireframeResource.class);
    }
}
