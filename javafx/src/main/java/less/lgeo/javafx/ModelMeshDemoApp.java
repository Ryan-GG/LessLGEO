package less.lgeo.javafx;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Triangle;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Vertex;
import less.lgeo.parse.Parser;
import java.io.File;
import java.io.IOException;

public class ModelMeshDemoApp extends Application {
    private static String ldrawFilePath;

    @Override
    public void start(Stage primaryStage) {
        if (ldrawFilePath == null) {
            System.err.println("No LDraw file path provided.");
            System.exit(1);
        }
        Model model;
        try {
            Parser parser = new Parser();
            model = parser.parse(new File(ldrawFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to parse LDraw file: " + ldrawFilePath);
            System.exit(1);
            return;
        }
        TriangleMesh mesh = ModelMeshBuilder.buildMesh(model);
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(Color.LIGHTBLUE));
        meshView.setDrawMode(DrawMode.FILL);
        meshView.setCullFace(CullFace.BACK);

        Group root = new Group(meshView);
        Scene scene = new Scene(root, 800, 600, true);
        scene.setFill(Color.GRAY);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
            new Rotate(-20, Rotate.X_AXIS),
            new Rotate(-20, Rotate.Y_AXIS),
            new Translate(0, 0, -400)
        );
        scene.setCamera(camera);

        primaryStage.setTitle("ModelMesh JavaFX Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ModelMeshDemoApp <LDraw file path>");
            System.exit(1);
        }
        ldrawFilePath = args[0];
        launch(args);
    }
} 