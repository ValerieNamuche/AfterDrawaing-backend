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

//Automl Google Imports
import com.google.cloud.automl.v1.AnnotationPayload;
import com.google.cloud.automl.v1.BoundingPoly;
import com.google.cloud.automl.v1.ExamplePayload;
import com.google.cloud.automl.v1.Image;
import com.google.cloud.automl.v1.ModelName;
import com.google.cloud.automl.v1.NormalizedVertex;
import com.google.cloud.automl.v1.PredictRequest;
import com.google.cloud.automl.v1.PredictResponse;
import com.google.cloud.automl.v1.PredictionServiceClient;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class WireframeServiceImpl implements WireframeService {

    @Autowired
    WireframeRepository wireframeRepository;
    @Autowired
    InterfaceRepository interfaceRepository;

    // Definimos una ArrayList para las clases
    List<String> classes = new ArrayList<String>();
    // Definimos una ArrayList para las posiciones
    //List<List<Float>> listvertex = new ArrayList<>();
    //x1
    List<Float> x1list = new ArrayList<Float>();
    //y1
    List<Float> y1list = new ArrayList<Float>();
    //x2
    List<Float> x2list = new ArrayList<Float>();
    //y2
    List<Float> y2list = new ArrayList<Float>();

    @Override
    public Page<Wireframe> getAllWireframes(Pageable pageable) {
        return wireframeRepository.findAll(pageable);
    }

    @Override
    public Wireframe getWireframeById(Long wireframeId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        return wireframe;
    }
    /*
    @Override
    public Wireframe updateWireframe(Long wireframeId, Wireframe WireframeRequest, Long interfaceId) {
        Interface anInterface= interfaceRepository.findById(interfaceId).orElseThrow(()-> new ResourceNotFoundException("Interface","Id", interfaceId));
        if (anInterface ==null){
            throw new ResourceNotFoundException("Invalid Interface, a Interface no exists with that value" );
        }
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        wireframe.setName(WireframeRequest.getName());
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
    */
    @Override
    public ResponseEntity<?> deleteWireframe(Long wireframeId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        wireframeRepository.delete(wireframe);
        return ResponseEntity.ok().build();
    }

    @Override
    public List<String> getClasses(String projectId, String modelId, byte[] content) throws IOException {

        try (PredictionServiceClient client = PredictionServiceClient.create()) {
            // Get the full path of the model.
            ModelName name = ModelName.of(projectId, "us-central1", modelId);
            ByteString imgBytes = ByteString.copyFrom(content);
            Image image = Image.newBuilder().setImageBytes(imgBytes).build();
            ExamplePayload payload = ExamplePayload.newBuilder().setImage(image).build();
            PredictRequest predictRequest =
                    PredictRequest.newBuilder()
                            .setName(name.toString())
                            .setPayload(payload)
                            .putParams(
                                    "score_threshold", "0.6") // [0.0-1.0] Only produce results higher than this value
                            .build();

            PredictResponse response = client.predict(predictRequest);

            Integer contador = 0;
            for (AnnotationPayload annotationPayload : response.getPayloadList()) {
                Integer verticeCount = 0;
                classes.add(annotationPayload.getDisplayName());

                //listvertex.add(new ArrayList<Float>());

                System.out.format("Predicted class name: %s\n", annotationPayload.getDisplayName());
                System.out.format(
                        "Predicted class score: %.2f\n",
                        annotationPayload.getImageObjectDetection().getScore());
                BoundingPoly boundingPoly = annotationPayload.getImageObjectDetection().getBoundingBox();
                System.out.println("Normalized Vertices:");
                for (NormalizedVertex vertex : boundingPoly.getNormalizedVerticesList()) {
                    if (verticeCount == 0){
                        x1list.add(vertex.getX());
                        y1list.add(vertex.getY());
                    }
                    else if(verticeCount == 1){
                        x2list.add(vertex.getX());
                        y2list.add(vertex.getY());
                    }

                    System.out.format("\tX: %.2f, Y: %.2f\n", vertex.getX(), vertex.getY());
                    verticeCount++;
                }
                contador++;
            }

        }
        return classes;
    }

    @Override
    public List<Float> getX1() {
        return x1list;
    }

    @Override
    public List<Float> getY1() {
        return y1list;
    }

    @Override
    public List<Float> getX2() {
        return x2list;
    }

    @Override
    public List<Float> getY2() {
        return y2list;
    }
}
