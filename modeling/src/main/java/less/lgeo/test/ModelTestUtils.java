package less.lgeo.test;

import static less.lgeo.common.VertexUtils.toVertex;
import static less.lgeo.primitive.QuadrilateralUtils.toQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.toTriangle;

import java.util.List;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Model.Builder;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;

/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {

  public static int INHERIT_VALUE_ID = 16;
  public static int INHERIT_EDGE_ID = 24;

  public static int BLACK_COLOR_ID = 1;
  public static int RED_COLOR_ID = 4;
  public static int ORANGE_COLOR_ID = 25;
  public static int YELLOW_COLOR_ID = 14;
  public static int GREEN_COLOR_ID = 2;
  public static int BLUE_COLOR_ID = 1;
  public static int INDIGO_COLOR_ID = 118;
  public static int VIOLET_COLOR_ID = 110;

  public static Builder newModelEmpty() {
    return Model.newBuilder();
  }

  public static Model.Builder cube() {

    Vertex a = toVertex(0, 0, 0);
    Vertex b = toVertex(0, 0, 1);
    Vertex c = toVertex(1, 0, 1);
    Vertex d = toVertex(1, 0, 0);
    Vertex e = toVertex(0, -1, 0);
    Vertex f = toVertex(0, -1, 1);
    Vertex g = toVertex(1, -1, 1);
    Vertex h = toVertex(1, -1, 0);

    Quadrilateral bottom = toQuadrilateral(ORANGE_COLOR_ID, a, b, c, d);
    Quadrilateral top = toQuadrilateral(RED_COLOR_ID, e, f, g, h);
    Quadrilateral front = toQuadrilateral(YELLOW_COLOR_ID, a, e, h, d);
    Quadrilateral back = toQuadrilateral(GREEN_COLOR_ID, b, f, g, c);
    Quadrilateral left = toQuadrilateral(BLUE_COLOR_ID, b, f, e, a);
    Quadrilateral right = toQuadrilateral(INDIGO_COLOR_ID, d, h, g, c);

    List<Quadrilateral> cube = List.of(bottom, top, front, back, left, right);

    return newModelEmpty()
        .addAllQuadrilateral(cube);
  }

  public static Model.Builder pyramid() {

    Vertex a = toVertex(0, 0, 0);
    Vertex b = toVertex(0, 0, 1);
    Vertex c = toVertex(1, 0, 1);
    Vertex d = toVertex(1, 0, 0);
    Vertex e = toVertex(0.5, -1, 0.5);

    Quadrilateral bottom = toQuadrilateral(VIOLET_COLOR_ID, a, b, c, d);
    Triangle front = toTriangle(RED_COLOR_ID, a, e, d);
    Triangle back = toTriangle(YELLOW_COLOR_ID, b, e, c);
    Triangle left = toTriangle(GREEN_COLOR_ID, a, e, b);
    Triangle right = toTriangle(BLACK_COLOR_ID, d, e, c);

    List<Triangle> pyramid = List.of(front, back, left, right);

    return newModelEmpty()
        .addAllTriangle(pyramid)
        .addQuadrilateral(bottom);
  }

}
