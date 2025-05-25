package less.lgeo.mesh;

import static less.lgeo.primitive.ModelUtils.getLines;
import static less.lgeo.primitive.ModelUtils.getOptionalLines;
import static less.lgeo.primitive.ModelUtils.getQuadrilaterals;
import static less.lgeo.primitive.ModelUtils.getTriangles;
import static less.lgeo.primitive.OptionalLineUtils.getVertices;

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

  private static final Float SPHERE_RADIUS = 0.05f;
  private static final Float LINE_WIDTH = 0.10f;

  private static final Color VERT_COLOR = Color.WHITE;
  private static final Color LINE_COLOR = Color.BLUE;
  private static final Color QUAD_COLOR = Color.GREEN;
  private static final Color TRIANGLE_COLOR = Color.YELLOW;
  private static final Color OPTIONAL_LINE_COLOR = Color.PURPLE;

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
    children.addAll(drawOptionalLines(model));
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
      point.setTranslateX(v.getX()); // scale for visibility
      point.setTranslateY(v.getY());
      point.setTranslateZ(v.getZ());
      point.setMaterial(new PhongMaterial(VERT_COLOR));
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
              .map(point -> new Point3D(point.x, point.y,
                  point.z)) // scale for visibility
              .toList();
          return new PolyLine3D(points, LINE_WIDTH, LINE_COLOR);
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
                  .map(point -> new Point3D(point.x, point.y,
                      point.z)) // scale for visibility
                  .collect(Collectors.toList()); // To modifiable list

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, QUAD_COLOR);
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
                  .map(point -> new Point3D(point.x, point.y,
                      point.z)) // scale for visibility
                  .collect(Collectors.toList()); // To modifiable list

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, TRIANGLE_COLOR);
            })
            .toList());

    return triangleGroup.getChildren();
  }

  private List<Node> drawOptionalLines(Model model) {
    Group optionalLineGroup = new Group();

    optionalLineGroup.getChildren().addAll(
        getOptionalLines(model).stream()
            .map(optionalLine -> {
              List<Point3D> points = getVertices(optionalLine).stream()
                  .map(RenderUtils::gpbToPoint3D)
                  .map(point -> new Point3D(point.x, point.y,
                      point.z)) // scale for visibility
                  .collect(Collectors.toList()); // To modifiable list

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, OPTIONAL_LINE_COLOR);
            })
            .toList());

    return optionalLineGroup.getChildren();
  }
}
