package com.afterdrawing.backendapi.controller;

import com.afterdrawing.backendapi.core.entity.Wireframe;
import com.afterdrawing.backendapi.core.service.WireframeService;
import com.afterdrawing.backendapi.resource.SaveWireframeResource;
import com.afterdrawing.backendapi.resource.WireframeResource;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Image imports
import com.afterdrawing.backendapi.core.repository.WireframeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import com.afterdrawing.backendapi.core.util.WireframeUtility;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.afterdrawing.backendapi.service.WireframeServiceImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class WireframeController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private WireframeService wireframeService;

    //upload image attr
    @Autowired
    WireframeRepository wireframeRepository;

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
            @PathVariable(name = "wireframeId") Long wireframeId) {
        return convertToResource(wireframeService.getWireframeById(wireframeId));
    }

    /*

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

    */
    @Operation( summary = "Delete Wireframe ", description = "Delete a Wireframe", tags = { "wireframes" })
    @DeleteMapping("/wireframes/{wireframeId}")
    public ResponseEntity<?> deleteWireframe(@PathVariable(name = "wireframeId") Long wireframeId) {
        return wireframeService.deleteWireframe(wireframeId);
    }

    //Image upload
    @PostMapping(value = "/upload/image", consumes = "multipart/form-data")
    public ResponseEntity<WireframeImageUploadResponse> uplaodImage(@RequestParam("file") MultipartFile file)
            throws JsonParseException, JsonMappingException, IOException {

        wireframeRepository.save(Wireframe.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .image(WireframeUtility.compressImage(file.getBytes()))
                .classes(wireframeService.getClasses("automl-test-345400", "IOD6723449832175828992", file.getBytes()))
                .X1(wireframeService.getX1())
                .Y1(wireframeService.getY1())
                .X2(wireframeService.getX2())
                .Y2(wireframeService.getY2()).build());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new WireframeImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }

    @GetMapping(path = {"/get/image/info/{name}"})
    public Wireframe getImageDetails(@PathVariable("name") String name) throws IOException {

        final Optional<Wireframe> dbImage = wireframeRepository.findByName(name);

        return Wireframe.builder()
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .image(WireframeUtility.decompressImage(dbImage.get().getImage()))
                .classes(dbImage.get().getClasses())
                .X1(dbImage.get().getX1())
                .Y1(dbImage.get().getY1())
                .X2(dbImage.get().getX2())
                .Y2(dbImage.get().getY2())
                .build();
    }

    @GetMapping(path = {"/get/image/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<Wireframe> dbImage = wireframeRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(WireframeUtility.decompressImage(dbImage.get().getImage()));
    }
    
    
    // Auto Mapper
    private Wireframe convertToEntity(SaveWireframeResource resource) {
        return mapper.map(resource, Wireframe.class);
    }

    private WireframeResource convertToResource(Wireframe entity) {
        return mapper.map(entity, WireframeResource.class);
    }
}
