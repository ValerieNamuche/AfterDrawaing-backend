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

import java.awt.desktop.OpenFilesHandler;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class WireframeServiceImpl implements WireframeService {

    @Autowired
    WireframeRepository wireframeRepository;
    @Autowired
    InterfaceRepository interfaceRepository;

    // Definimos una ArrayList para guardar las clases encontradas
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


    //code
    List<String> codigo = new ArrayList<String>();

    @Override
    public Page<Wireframe> getAllWireframes(Pageable pageable) {
        return wireframeRepository.findAll(pageable);
    }

    @Override
    public Wireframe getWireframeById(Long wireframeId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        return wireframe;
    }

    @Override
    public ResponseEntity<?> deleteWireframe(Long wireframeId) {
        Wireframe wireframe = wireframeRepository.findById(wireframeId).orElseThrow(() -> new ResourceNotFoundException("Wireframe", "Id", wireframeId));
        wireframeRepository.delete(wireframe);
        return ResponseEntity.ok().build();

    }

    @Override
    public List<String> getClasses(String projectId, String modelId, byte[] content) throws IOException {
        boolean empty = classes.isEmpty();
        //boolean notEmpty = Files.notExists(p);

        if (empty) {
            System.out.println("Lists are empty");

        } else {
            System.out.println("List are not empty!");
            classes.clear();
            x1list.clear();
            x2list.clear();
            y1list.clear();
            y2list.clear();
        }

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

    @Override
    public List<String> getWireframeCode() throws IOException {
        //creo una lista con todas las clases y la comprar con la lista "clases" y voy agregando a la lista "codigo" el codigo de las clases encontradas
        //Lista de todas las clases
        //List<String> lista_clases = Arrays.asList("CheckedTextView", "EditText", "Icon", "Image", "Text", "TextButton");

        //for (String clas: classes) {
        //
        //}
        Path p = Paths.get("Code.txt");
        boolean exists = Files.exists(p);
        boolean notExists = Files.notExists(p);

        if (exists) {
            System.out.println("File exists!");
            System.out.println("File will be deleted!");
            Files.delete(p);
            System.out.println("File deleted!");
        } else if (notExists) {
            System.out.println("File doesn't exist!");
        } else {
            System.out.println("File's status is unknown!");
        }

        boolean CodeEmpty = codigo.isEmpty();
        //boolean notEmpty = Files.notExists(p);

        if (CodeEmpty) {
            System.out.println("Code List is empty");

        } else {
            System.out.println("List are not empty!");
            codigo.clear();
        }

        codigo.add("import 'package:flutter/material.dart';");
        codigo.add("void main() => runApp(const MyApp());");

        codigo.add("class MyApp extends StatelessWidget");
        codigo.add("{");
        codigo.add("    const MyApp({Key? key}) : super(key: key);");
        codigo.add("    @override");
        codigo.add("    Widget build(BuildContext context)");
        codigo.add("    {");
        codigo.add("        return MaterialApp");
        codigo.add("            (");
        codigo.add("            home: Scaffold");
        codigo.add("                (");
        codigo.add("                appBar: AppBar(),");
        codigo.add("                body: Stack");
        codigo.add("                    (");
        codigo.add("                    children: <Widget>");
        codigo.add("                        [");


        for (String clase : classes) {
            if (clase.equals("CheckedTextView")){
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            alignment: Alignment.center,");//Cambiar posicion de acuerdo a la posicicion
                codigo.add("                            padding: const EdgeInsets.all(10),");
                codigo.add("                            child: Checkbox");
                codigo.add("                                (");
                codigo.add("                                value: false,");
                codigo.add("                                onChanged: (bool? value) {}");
                codigo.add("                                )");
                codigo.add("                            ),");

            }
            else if (clase.equals("EditText")) {
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            alignment: Alignment.bottomCenter,");//Cambiar posicion de acuerdo a la posicicion
                codigo.add("                            padding: const EdgeInsets.all(10),");
                codigo.add("                            child: TextField");
                codigo.add("                                (");
                codigo.add("                                decoration: const InputDecoration");
                codigo.add("                                    (");
                codigo.add("                                    border: OutlineInputBorder(),");
                codigo.add("                                    labelText: 'Input Text',");
                codigo.add("                                    )");
                codigo.add("                                )");
                codigo.add("                            ),");

            }
            else if (clase.equals("Icon")) {
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            alignment: Alignment.bottomCenter,");//Cambiar posicion de acuerdo a la posicicion
                codigo.add("                            padding: const EdgeInsets.all(10),");
                codigo.add("                            child: Icon");
                codigo.add("                                (");
                codigo.add("                                Icons.mail_outline,");//Cambiar icono
                codigo.add("                                size: 80.0,");
                codigo.add("                                ),");
                codigo.add("                            ),");

            }
            else if (clase.equals("Image")) {
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            alignment: Alignment.bottomCenter,");//Cambiar posicion de acuerdo a la posicicion
                codigo.add("                            height: 120.0,");
                codigo.add("                            width: 120.0,");
                codigo.add("                            decoration: BoxDecoration");
                codigo.add("                                (");
                codigo.add("                                image: DecorationImage");
                codigo.add("                                    (");
                codigo.add("                                    image: AssetImage('path/image.jpg'),");
                codigo.add("                                    fit: BoxFit.fill,");
                codigo.add("                                    )");
                codigo.add("                                    shape: BoxShape.circle,");
                codigo.add("                                )");
                codigo.add("                            ),");

            }
            else if (clase.equals("Text")) {
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            child: Align");
                codigo.add("                                (");
                codigo.add("                                alignment: Alignment.centerRight,");
                codigo.add("                                child: Text");
                codigo.add("                                    (");
                codigo.add("                                    'Some text here',");
                codigo.add("                                    style: TextStyle(),");
                codigo.add("                                    )");
                codigo.add("                                )");
                codigo.add("                            ),");

            }
            else if (clase.equals("TextButton")) {
                codigo.add("                        Container");
                codigo.add("                            (");
                codigo.add("                            alignment: Alignment.topRight,");//Cambiar posicion de acuerdo a la posicicion
                codigo.add("                            padding: const EdgeInsets.all(10),");
                codigo.add("                            child: TextButton");
                codigo.add("                                (");
                codigo.add("                                onPressed: ()");
                codigo.add("                                {");
                codigo.add("                                    //Do Something");
                codigo.add("                                },");
                codigo.add("                                child: const Text('Insert Text',),");
                codigo.add("                                style: TextButton.styleFrom");
                codigo.add("                                    (");
                codigo.add("                                    primary: Colors.white,");
                codigo.add("                                    backgroundColor: Colors.teal,");
                codigo.add("                                    )");
                codigo.add("                                )");
                codigo.add("                            ),");

            }
            else {
                codigo.add("No coicidio la clase");
            }
        }
        codigo.add("                        ]");
        codigo.add("                    )");
        codigo.add("                )");
        codigo.add("            );");
        codigo.add("    }");
        codigo.add("}");

        FileWriter outFile = new FileWriter("Code.txt",true);
        BufferedWriter outStream = new BufferedWriter(outFile);
        for (int i = 0; i < codigo.size(); i++) {
            outStream.write(codigo.get(i));
            outStream.newLine();
        }
        //text file
        //File initialFile = new File("Code.txt");
        //InputStream targetStream = new FileInputStream(initialFile);


        outStream.close();
        //return targetStream
        return codigo;
    }
}
