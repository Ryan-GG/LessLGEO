package less.lgeo;

import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraController {

  private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
  private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
  private final Translate translate = new Translate(0, 0, -500);
  private double mouseOldX, mouseOldY;
  private boolean shiftDown = false;

  public CameraController(PerspectiveCamera camera, Scene scene) {
    camera.getTransforms().addAll(rotateX, rotateY, translate);
    setupControls(scene);
  }

  private void setupControls(Scene scene) {
    // Mouse Press
    scene.setOnMousePressed(event -> {
      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();
      shiftDown = event.isShiftDown();
    });

    // Mouse Drag
    scene.setOnMouseDragged(event -> {
      double dx = event.getSceneX() - mouseOldX;
      double dy = event.getSceneY() - mouseOldY;
      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();

      if (shiftDown) {
        // Pan
        translate.setX(translate.getX() + dx * 0.5);
        translate.setY(translate.getY() + dy * 0.5);
      } else {
        // Rotate
        rotateY.setAngle(rotateY.getAngle() + dx * 0.3);
        rotateX.setAngle(rotateX.getAngle() - dy * 0.3);
      }
    });

    // Scroll (Zoom)
    scene.setOnScroll(event -> {
      double zoom = event.getDeltaY();
      translate.setZ(translate.getZ() + (zoom > 0 ? 20 : -20));
      translate.setZ(Math.min(-50, Math.max(-2000, translate.getZ())));
    });

    // Keyboard Pan
    scene.setOnKeyPressed(event -> {
      switch (event.getCode()) {
        case UP -> translate.setY(translate.getY() - 10);
        case DOWN -> translate.setY(translate.getY() + 10);
        case LEFT -> translate.setX(translate.getX() - 10);
        case RIGHT -> translate.setX(translate.getX() + 10);
      }
    });
  }
}
