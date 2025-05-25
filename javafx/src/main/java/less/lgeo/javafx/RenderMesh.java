package less.lgeo.javafx;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import less.lgeo.camera.CameraController;
import less.lgeo.mesh.ModelMesh;
import less.lgeo.parse.Parser;
import less.lgeo.primitive.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderMesh extends Application {

  private static final String TITLE = "Less LGEO RenderMesh";
  private static final Logger logger = LoggerFactory.getLogger(RenderMesh.class);

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

    logger.info("Rendering Mesh");

    AmbientLight ambientLight = new AmbientLight(Color.color(1, 1, 1));

    ModelMesh modelMesh = new ModelMesh(model);
    Group world = new Group(ambientLight, modelMesh.getMesh());

    Scene scene = new Scene(world, 800, 600, true, SceneAntialiasing.BALANCED);
    attachCamera(scene);

    setUpStage(stage, scene);
  }

  private void setUpStage(Stage stage, Scene scene) {
    stage.setTitle(TITLE);
    stage.setScene(scene);
    stage.show();
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