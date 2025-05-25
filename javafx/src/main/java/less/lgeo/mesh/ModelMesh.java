package less.lgeo.mesh;

import static less.lgeo.primitive.ModelUtils.getLines;
import static less.lgeo.primitive.ModelUtils.getQuadrilaterals;
import static less.lgeo.primitive.ModelUtils.getTriangles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import less.lgeo.primitive.LineUtils;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.QuaderilateralUtils;
import less.lgeo.primitive.TriangleUtils;
import less.lgeo.primitive.Vertex;
import less.lgeo.utils.RenderUtils;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;

public class ModelMesh {

  private static final Float SPHERE_RADIUS = 1.0f;
  private static final Float LINE_WIDTH = 1.0f;
  private static final Color MESH_COLOR = Color.BLUE;
  private Group mesh;

  public ModelMesh(Model model) {
    setMesh(model);
  }

  public Group getMesh() {
    return this.mesh;
  }

  private void setMesh(Model model) {
    List<Node> children = new ArrayList<>();
    children.addAll(drawVertices(model));
    children.addAll(drawLines(model));
    children.addAll(drawQuadrilaterals(model));
    children.addAll(drawTriangles(model));
    mesh = new Group(children);
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link Vertex} as JavaFx {@link Node}
   */
  private List<Node> drawVertices(Model model) {
    Group verticesGroup = new Group();
    Set<Vertex> vertexSet = ModelUtils.getVertices(model);

    for (Vertex v : vertexSet) {
      Sphere point = new Sphere(SPHERE_RADIUS);
      point.setTranslateX(v.getX() * 10); // scale for visibility
      point.setTranslateY(v.getY() * 10);
      point.setTranslateZ(v.getZ() * 10);
      point.setMaterial(new PhongMaterial(MESH_COLOR));
      verticesGroup.getChildren().add(point);
    }

    return verticesGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Line} as JavaFX {@link Node}
   */
  private List<Node> drawLines(Model model) {
    Group lineGroup = new Group();
    lineGroup.getChildren().addAll(getLines(model).stream()
        .map(line -> {
          List<Point3D> points = LineUtils.getVertices(line).stream()
              .map(RenderUtils::gpbToPoint3D)
              .map(point -> new Point3D(point.x * 10, point.y * 10,
                  point.z * 10)) // scale for visibility
              .toList();
          return new PolyLine3D(points, LINE_WIDTH, MESH_COLOR);
        })
        .toList());

    return lineGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Quadrilateral} as JavaFX {@link Node}
   */
  private List<Node> drawQuadrilaterals(Model model) {
    Group quadrilateralGroup = new Group();
    quadrilateralGroup.getChildren().addAll(
        getQuadrilaterals(model).stream()
            .map(quadrilateral -> {
              List<Point3D> points = QuaderilateralUtils.getVertices(quadrilateral).stream()
                  .map(RenderUtils::gpbToPoint3D)
                  .map(point -> new Point3D(point.x * 10, point.y * 10,
                      point.z * 10)) // scale for visibility
                  .collect(Collectors.toList()); // To modifiable list

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, MESH_COLOR);
            })
            .toList());

    return quadrilateralGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Triangle} as JavaFX {@link Node}
   */
  private List<Node> drawTriangles(Model model) {
    Group triangleGroup = new Group();

    triangleGroup.getChildren().addAll(
        getTriangles(model).stream()
            .map(triangle -> {
              List<Point3D> points = TriangleUtils.getVertices(triangle).stream()
                  .map(RenderUtils::gpbToPoint3D)
                  .map(point -> new Point3D(point.x * 10, point.y * 10,
                      point.z * 10)) // scale for visibility
                  .collect(Collectors.toList()); // To modifiable list

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, MESH_COLOR);
            })
            .toList());

    return triangleGroup.getChildren();
  }
}
