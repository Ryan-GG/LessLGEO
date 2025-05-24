package less.lgeo.javafx;

import static less.lgeo.primitive.ModelUtils.getLines;
import static less.lgeo.primitive.ModelUtils.getVertices;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import less.lgeo.camera.CameraController;
import less.lgeo.parse.Parser;
import less.lgeo.primitive.LineUtils;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Vertex;
import less.lgeo.utils.RenderUtils;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;

public class RenderMesh extends Application {

  private static final String TITLE = "Less LGEO RenderMesh";

  public static void main(String[] args) {
    if (args.length < 1) {
      System.err.println("Usage: java ModelMeshDemoApp <LDraw file path>");
      System.exit(1);
    }
    launch(args);
  }

  @Override
  public void start(Stage stage) {

    File fileToParse = new File(getParameters().getRaw().getFirst());
    Model model = getModel(fileToParse).orElseThrow();

    AmbientLight ambientLight = new AmbientLight(Color.color(1, 1, 1));
    Group modelVerticiesGroup = getModelVerticiesGroup(model);

    List<PolyLine3D> lines = getModelLines(model);
    Group world = new Group(modelVerticiesGroup, ambientLight);
    world.getChildren().addAll(lines);

    Scene scene = new Scene(world, 800, 600, true, SceneAntialiasing.BALANCED);
    attachCamera(scene);

    setUpStage(stage, scene);
  }

  private void setUpStage(Stage stage, Scene scene) {
    stage.setTitle(TITLE);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * @param model Model containing vertices
   * @return A group of all vertices of the parsed {@link Model} Gpb
   */
  private Group getModelVerticiesGroup(Model model) {
    Group meshGroup = new Group();
    Set<Vertex> vertexSet = getVertices(model);

    for (Vertex v : vertexSet) {
      Sphere point = new Sphere(1.0); // tiny sphere per point
      point.setTranslateX(v.getX() * 10); // scale for visibility
      point.setTranslateY(v.getY() * 10);
      point.setTranslateZ(v.getZ() * 10);
      point.setMaterial(new PhongMaterial(Color.BLUE));
      meshGroup.getChildren().add(point);
    }

    return meshGroup;
  }

  private List<PolyLine3D> getModelLines(Model model) {
    return getLines(model).stream()
        .map(line -> {
          List<Point3D> points = LineUtils.getVertices(line).stream()
              .map(RenderUtils::gpbToFx)
              .map(p -> new Point3D(p.x * 10, p.y * 10, p.z * 10)) // scale for visibility
              .toList();
          return new PolyLine3D(points, 5.0f, Color.BLUE);
        })
        .toList();
  }

  private void attachCamera(Scene scene) {
    scene.setFill(Color.BLACK);
    CameraController cameraController = new CameraController(scene);
    Camera camera = cameraController.getCamera();
    scene.setCamera(camera);
  }

  /**
   * @param file LDraw File to Parse
   * @return {@link Model} representations
   */
  private Optional<Model> getModel(File file) {
    try {
      return Optional.of(new Parser().parse(file));
    } catch (IOException e) {
      return Optional.empty();
    }
  }
} 