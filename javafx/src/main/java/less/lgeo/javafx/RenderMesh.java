package less.lgeo.javafx;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "less.lgeo")
public class RenderMesh extends Application {

  private static final String TITLE = "Less LGEO RenderMesh";
  private static final Logger logger = LoggerFactory.getLogger(RenderMesh.class);
  private ConfigurableApplicationContext applicationContext;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    applicationContext = SpringApplication.run(RenderMesh.class);
  }

  @Override
  public void start(Stage stage) {

    Parameters parameters = getParameters();
    if (parameters == null || parameters.getRaw().isEmpty()) {
      throw new IllegalArgumentException("Usage: RenderMesh <Model UUID>");
    }
    logger.info("Rendering Mesh");

    AmbientLight ambientLight = new AmbientLight(Color.color(1, 1, 1));

    // Use ModelMeshFactory from Spring context
    less.lgeo.mesh.ModelMeshFactory modelMeshFactory = applicationContext.getBean(
        less.lgeo.mesh.ModelMeshFactory.class);
    ModelMesh modelMesh = modelMeshFactory.create(parameters.getRaw().getFirst());
    Group world = new Group(ambientLight, modelMesh.getMesh());

    Scene scene = new Scene(world, 800, 600, true, SceneAntialiasing.BALANCED);
    attachCamera(scene);

    setUpStage(stage, scene);
    logger.info("Showing Mesh");
  }

  private void setUpStage(Stage stage, Scene scene) {
    stage.setTitle(TITLE);
    stage.setScene(scene);
    stage.show();
  }

  private void attachCamera(Scene scene) {
    scene.setFill(Color.GRAY);
    CameraController cameraController = new CameraController(scene);
    Camera camera = cameraController.getCamera();
    scene.setCamera(camera);
  }

  @Override
  public void stop() {
    applicationContext.close();
  }
} 