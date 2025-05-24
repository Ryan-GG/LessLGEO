package less.lgeo.javafx;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import less.lgeo.primitive.*;
import less.lgeo.parse.Parser;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class ModelMeshDemoApp extends Application {

    @Override
    public void start(Stage stage) {

        AmbientLight ambientLight = new AmbientLight(Color.color(1, 1, 1));

        // Group for all rendered points
        Group pointsGroup = new Group();

        File fileToParse = new File( getParameters().getRaw().getFirst() );
        Model model = getModel( fileToParse ) .orElseThrow();

        Set<Vertex> vertexSet = ModelUtils.getVerticies(model);

        for (Vertex v : vertexSet) {
            Sphere point = new Sphere(1.0); // tiny sphere per point
            point.setTranslateX(v.getX() * 10); // scale for visibility
            point.setTranslateY(v.getY() * 10);
            point.setTranslateZ(v.getY() * 10);
            point.setMaterial(new PhongMaterial(Color.BLUE));
            pointsGroup.getChildren().add(point);
        }

        // World group for transforms
        Group world = new Group(pointsGroup, ambientLight);

        // Camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        camera.setTranslateZ(-500);
        camera.getTransforms().addAll(
                new Rotate(-20, Rotate.X_AXIS),
                new Rotate(-20, Rotate.Y_AXIS),
                new Translate(0, 0, -400)
        );

        // Scene
        Scene scene = new Scene(world, 800, 600, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setCamera(camera);

        stage.setTitle("3D Points Visualization");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ModelMeshDemoApp <LDraw file path>");
            System.exit(1);
        }
        launch(args);
    }

    /**
     *
     * @param file
     * @return
     */
    private Optional<Model> getModel(File file )
    {
        try {
            return Optional.of( new Parser().parse( file ) );
        } catch (IOException e) {
            return Optional.empty();
        }
    }
} 