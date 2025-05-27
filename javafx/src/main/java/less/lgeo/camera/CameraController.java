package less.lgeo.camera;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraController {

  private final Camera camera;
  private final Scene scene;
  private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
  private final Rotate rotateY = new Rotate(-20, Rotate.Y_AXIS);
  private final Translate translate = new Translate(0, 0, -400);
  private double anchorX, anchorY;
  private double anchorAngleX = -20, anchorAngleY = -20;

  public CameraController(Scene scene) {
    this.camera = new PerspectiveCamera(true);
    this.scene = scene;
    this.camera.getTransforms().clear();
    this.camera.getTransforms().addAll(rotateX, rotateY, translate);
    enable();
  }

  public Camera getCamera() {
    return this.camera;
  }

  public void enable() {
    this.camera.setNearClip(0.1);
    this.camera.setFarClip(10000);
    this.camera.setTranslateZ(-10);
    scene.setOnMousePressed(this::onMousePressed);
    scene.setOnMouseDragged(this::onMouseDragged);
    scene.setOnScroll(this::onScroll);
    scene.setOnKeyPressed(this::onKeyPressed);
  }

  private void onMousePressed(MouseEvent event) {
    anchorX = event.getSceneX();
    anchorY = event.getSceneY();
    anchorAngleX = rotateX.getAngle();
    anchorAngleY = rotateY.getAngle();
  }

  private void onMouseDragged(MouseEvent event) {
    double deltaX = event.getSceneX() - anchorX;
    double deltaY = event.getSceneY() - anchorY;
    rotateY.setAngle(anchorAngleY + deltaX * 0.5);
    rotateX.setAngle(anchorAngleX - deltaY * 0.5);
  }

  private void onScroll(ScrollEvent event) {
    double zoom = event.getDeltaY();
    translate.setZ(translate.getZ() + zoom * 0.5);
  }

  private void onKeyPressed(KeyEvent event) {
    double moveAmount = 10;
    switch (event.getCode()) {
      case UP, W -> translate.setY(translate.getY() - moveAmount);
      case DOWN, S -> translate.setY(translate.getY() + moveAmount);
      case LEFT, A -> translate.setX(translate.getX() - moveAmount);
      case RIGHT, D -> translate.setX(translate.getX() + moveAmount);
      default -> {
      }
    }
  }

}
