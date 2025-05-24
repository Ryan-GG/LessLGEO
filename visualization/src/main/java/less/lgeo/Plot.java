package less.lgeo;

import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class Plot extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {

    AmbientLight ambientLight = new AmbientLight(Color.color(1, 1, 1));

    // Group for all rendered points
    Group pointsGroup = new Group();

    double[][] coords = {
        {0.9239, 1.0, 0.3827},
        {0.7071, 1.0, 0.7071},
        {-0.7071, 1.0, 0.7071},
        {-0.9239, 1.0, 0.3827},
        {-0.9239, 1.0, -0.3827},
        {-0.7071, 1.0, -0.7071},
        {0.7071, 1.0, -0.7071},
        {0.9239, 1.0, -0.3827},
        {-10.0, 24.0, -10.0},
        {10.0, 24.0, -10.0},
        {10.0, 24.0, 10.0},
        {-10.0, 24.0, 10.0},
        {0.0, 0.0, 0.0},
        {1.0, 1.0, 1.0},
        {-1.0, 1.0, 1.0},
        {-1.0, 1.0, -1.0},
        {1.0, 1.0, -1.0},
        {-6.0, 24.0, -6.0},
        {6.0, 24.0, -6.0},
        {6.0, 24.0, 6.0},
        {-6.0, 24.0, 6.0},
        {0.3827, 0.0, 0.9239},
        {-0.3827, 0.0, 0.9239},
        {-0.3827, 0.0, -0.9239},
        {0.3827, 0.0, -0.9239},
        {1.0, 1.0, 0.0},
        {0.0, 1.0, 1.0},
        {-1.0, 1.0, 0.0},
        {0.0, 1.0, -1.0},
        {1.0, 0.0, 1.0},
        {-1.0, 0.0, 1.0},
        {-1.0, 0.0, -1.0},
        {1.0, 0.0, -1.0},
        {10.0, 0.0, -10.0},
        {-10.0, 0.0, -10.0},
        {0.3827, 1.0, 0.9239},
        {-0.3827, 1.0, 0.9239},
        {-0.3827, 1.0, -0.9239},
        {0.3827, 1.0, -0.9239},
        {0.7071, 0.0, 0.7071},
        {-0.7071, 0.0, 0.7071},
        {-0.7071, 0.0, -0.7071},
        {0.7071, 0.0, -0.7071},
        {0.9239, 0.0, 0.3827},
        {-0.9239, 0.0, 0.3827},
        {-0.9239, 0.0, -0.3827},
        {0.9239, 0.0, -0.3827},
        {1.0, 0.0, 0.0},
        {0.0, 0.0, 1.0},
        {-1.0, 0.0, 0.0},
        {0.0, 0.0, -1.0}
    };

    for (double[] c : coords) {
      Sphere point = new Sphere(1.0); // tiny sphere per point
      point.setTranslateX(c[0] * 10); // scale for visibility
      point.setTranslateY(c[1] * 10); // flip Y for intuitive view
      point.setTranslateZ(c[2] * 10);
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

    // Scene
    Scene scene = new Scene(world, 800, 600, true, SceneAntialiasing.BALANCED);
    scene.setFill(Color.BLACK);
    scene.setCamera(camera);

    new CameraController(camera, scene);

    stage.setTitle("3D Points Visualization");
    stage.setScene(scene);
    stage.show();
  }
}
