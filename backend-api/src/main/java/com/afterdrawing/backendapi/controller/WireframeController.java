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
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Image imports
import com.afterdrawing.backendapi.core.repository.WireframeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import com.afterdrawing.backendapi.core.util.WireframeUtility;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Operation( summary = "Delete Wireframe ", description = "Delete a Wireframe", tags = { "wireframes" })
    @DeleteMapping("/wireframes/{wireframeId}")
    public ResponseEntity<?> deleteWireframe(@PathVariable(name = "wireframeId") Long wireframeId) {
        return wireframeService.deleteWireframe(wireframeId);
    }

    //Image upload
    @Operation(summary = "Upload Wireframe ", description = "Upload a Wireframe Image", tags = { "wireframes" })
    @PostMapping(value = "/upload/image", consumes = "multipart/form-data")
    public ResponseEntity<WireframeImageUploadResponse> uplaodImage(@RequestParam("file") MultipartFile file)
            throws JsonParseException, JsonMappingException, IOException {

        wireframeRepository.save(Wireframe.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .classes(wireframeService.getClasses("green-wares-350602", "IOD1693424928147111936", file.getBytes()))
                .X1(wireframeService.getX1())
                .Y1(wireframeService.getY1())
                .X2(wireframeService.getX2())
                .Y2(wireframeService.getY2())
                .code(wireframeService.getWireframeCode())
                .image(WireframeUtility.compressImage(wireframeService.getImage()))
                .build());


        return ResponseEntity.status(HttpStatus.OK)
                .body(new WireframeImageUploadResponse("Image uploaded successfully: " +
                        file.getOriginalFilename()));
    }
    @Operation(summary = "Get Wireframe Information", description = "Get Wireframe Information by specifying name", tags = { "wireframes" })
    @GetMapping(path = {"/get/wireframe/info/{name}"})
    public Wireframe getImageDetails(@PathVariable("name") String name) throws IOException {

        final Optional<Wireframe> dbImage = wireframeRepository.findByName(name);

        return Wireframe.builder()
                .id(dbImage.get().getId())
                .name(dbImage.get().getName())
                .type(dbImage.get().getType())
                .classes(dbImage.get().getClasses())
                .X1(dbImage.get().getX1())
                .Y1(dbImage.get().getY1())
                .X2(dbImage.get().getX2())
                .Y2(dbImage.get().getY2())
                .code(dbImage.get().getCode())
                .build();
    }
    @Operation(summary = "Get Wireframe Image", description = "Get Wireframe Image by specifying name", tags = { "wireframes" })
    @GetMapping(path = {"/get/wireframe/{name}"})
    public ResponseEntity<byte[]> getImage(@PathVariable("name") String name) throws IOException {

        final Optional<Wireframe> dbImage = wireframeRepository.findByName(name);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbImage.get().getType()))
                .body(WireframeUtility.decompressImage(dbImage.get().getImage()));
    }

    @Operation(summary = "Get Wireframe Code File", description = "Download the wireframe code file ", tags = { "wireframes" })
    @RequestMapping(path = "/get/wireframe/code/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download() throws IOException {

        // ...
        HttpHeaders headers = new HttpHeaders(); headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Code.txt");
        InputStreamResource resource = new InputStreamResource(new FileInputStream("Code.txt"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private Wireframe convertToEntity(SaveWireframeResource resource) {
        return mapper.map(resource, Wireframe.class);
    }

    private WireframeResource convertToResource(Wireframe entity) {
        return mapper.map(entity, WireframeResource.class);
    }
}
