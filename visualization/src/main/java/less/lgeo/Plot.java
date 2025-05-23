package less.lgeo;

import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Plot extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    Sphere sphere = new Sphere(50);
    PhongMaterial material = new PhongMaterial(Color.RED);
    sphere.setMaterial(material);

    PointLight light = new PointLight(Color.WHITE);
    light.setTranslateX(-100);
    light.setTranslateY(-100);
    light.setTranslateZ(-100);

    AmbientLight ambientLight = new AmbientLight(Color.color(0.3, 0.3, 0.3));

    Group root = new Group(sphere, light, ambientLight);

    // Create a sub-group to apply rotations to
    Group world = new Group(root);

    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.setTranslateZ(-300);
    camera.setNearClip(0.1);
    camera.setFarClip(1000);

    Scene scene = new Scene(world, 600, 400, true, SceneAntialiasing.BALANCED);
    scene.setFill(Color.GRAY);
    scene.setCamera(camera);

    // Variables to track mouse position and rotation
    final double[] mouseOldX = new double[1];
    final double[] mouseOldY = new double[1];
    final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    root.getTransforms().addAll(rotateX, rotateY);

    scene.setOnMousePressed(event -> {
      mouseOldX[0] = event.getSceneX();
      mouseOldY[0] = event.getSceneY();
    });

    scene.setOnMouseDragged(event -> {
      double deltaX = event.getSceneX() - mouseOldX[0];
      double deltaY = event.getSceneY() - mouseOldY[0];
      rotateY.setAngle(rotateY.getAngle() + deltaX * 0.5);
      rotateX.setAngle(rotateX.getAngle() - deltaY * 0.5);
      mouseOldX[0] = event.getSceneX();
      mouseOldY[0] = event.getSceneY();
    });

    scene.setOnScroll(event -> {
      double delta = event.getDeltaY();
      double currentZ = camera.getTranslateZ();
      double newZ = currentZ + (delta > 0 ? 20 : -20);
      // Clamp zoom
      if (newZ < -1000) {
        newZ = -1000;
      }
      if (newZ > -50) {
        newZ = -50;
      }
      camera.setTranslateZ(newZ);
    });

    stage.setTitle("3D Sphere with Mouse Controls");
    stage.setScene(scene);
    stage.show();
  }


}
