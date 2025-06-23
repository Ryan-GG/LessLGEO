package less.lgeo.mesh;

import static javafx.scene.shape.VertexFormat.POINT_TEXCOORD;
import static less.lgeo.connection.ConnectionUtils.getConnectionPoints;
import static less.lgeo.primitive.ModelUtils.getConnections;
import static less.lgeo.primitive.ModelUtils.getLines;
import static less.lgeo.primitive.ModelUtils.getOptionalLines;
import static less.lgeo.primitive.ModelUtils.getQuadrilaterals;
import static less.lgeo.primitive.ModelUtils.getTriangles;
import static less.lgeo.primitive.OptionalLineUtils.getVertices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.LineUtils;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.QuadrilateralUtils;
import less.lgeo.primitive.TriangleUtils;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;
import org.fxyz3d.shapes.primitives.CubeMesh;

public class ModelMesh {

  private static final Float CONN_SIZE = 1.0f;
  private static final Float SPHERE_RADIUS = 0.5f;
  private static final Float LINE_WIDTH = 0.5f;

  private static final Color CONN_COLOR = Color.DEEPPINK;
  private static final Color VERT_COLOR = Color.WHITE;
  private static final Color LINE_COLOR = Color.BLUE;
  private static final Color QUAD_COLOR = Color.GREEN;
  private static final Color TRIANGLE_COLOR = Color.YELLOW;
  private static final Color OPTIONAL_LINE_COLOR = Color.PURPLE;

  private Group mesh;

  public ModelMesh(Model model) {
    setMesh(model);
  }

  public static Point3D gpbToPoint3D(Vertex point) {
    return new Point3D(point.getX(), point.getY(), point.getZ());
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
    children.addAll(drawConnections(model));
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
      point.setTranslateX(v.getX());
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
              .map(ModelMesh::gpbToPoint3D)
              .map(point -> new Point3D(point.x, point.y,
                  point.z))
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
              List<Point3D> points = QuadrilateralUtils.getVertices(quadrilateral).stream()
                  .map(ModelMesh::gpbToPoint3D)
                  .map(point -> new Point3D(point.x, point.y,
                      point.z))
                  .collect(Collectors.toList());

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
              /*List<Point3D> points = TriangleUtils.getVertices( triangle ).stream()
                  .map( ModelMesh::gpbToPoint3D )
                  .map( point -> new Point3D( point.x, point.y,
                      point.z ) )
                  .collect( Collectors.toList() );

              // Add first point again to close loop
              points.add( points.getFirst() );

              return new PolyLine3D( points, LINE_WIDTH, TRIANGLE_COLOR );*/

              List<Float> trianglePoints = TriangleUtils.getVertices(triangle).stream()
                  .flatMap(vertex -> Stream.of(vertex.getX(), vertex.getY(), vertex.getZ()))
                  .map(Double::floatValue).toList();

              float[] array = new float[trianglePoints.size()];
              for (int i = 0; i < trianglePoints.size(); i++) {
                array[i] = trianglePoints.get(i);
              }

              TriangleMesh triangleMesh = new TriangleMesh(POINT_TEXCOORD);
              triangleMesh.getPoints().addAll(array);

              triangleMesh.getTexCoords().addAll(0, 0);

              // One triangle face: uses point indices and texCoord indices
              triangleMesh.getFaces().addAll(
                  0, 0, 1, 0, 2, 0
              );

              MeshView triangleView = new MeshView(triangleMesh);

              triangleView.setMaterial(new PhongMaterial(Color.RED));

              return triangleView;
            })
            .toList());

    return triangleGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.OptionalLine} as JavaFX {@link Node}
   */
  private List<Node> drawOptionalLines(Model model) {
    Group optionalLineGroup = new Group();

    optionalLineGroup.getChildren().addAll(
        getOptionalLines(model).stream()
            .map(optionalLine -> {
              List<Point3D> points = getVertices(optionalLine).stream()
                  .map(ModelMesh::gpbToPoint3D)
                  .map(point -> new Point3D(point.x, point.y,
                      point.z))
                  .collect(Collectors.toList());

              // Add first point again to close loop
              points.add(points.getFirst());

              return new PolyLine3D(points, LINE_WIDTH, OPTIONAL_LINE_COLOR);
            })
            .toList());

    return optionalLineGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.connectivity.Connection} as JavaFX {@link Node}
   */
  private List<Node> drawConnections(Model model) {
    Group connectionGroup = new Group();

    Set<Vertex> vertexSet = getConnections(model).stream()
        .flatMap(connection -> getConnectionPoints(connection).stream())
        .collect(Collectors.toSet());

    for (Vertex vertex : vertexSet) {
      CubeMesh connectionPoint = new CubeMesh(CONN_SIZE);
      connectionPoint.setTranslateX(vertex.getX());
      connectionPoint.setTranslateY(vertex.getY());
      connectionPoint.setTranslateZ(vertex.getZ());
      connectionPoint.setMaterial(new PhongMaterial(CONN_COLOR));
      connectionGroup.getChildren().add(connectionPoint);
    }

    return connectionGroup.getChildren();
  }

}
