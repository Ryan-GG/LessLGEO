package less.lgeo.primitive;

import java.util.List;

public class TriangleUtils {

  public static Triangle getTriangle(Color color, Vertex p1, Vertex p2, Vertex p3) {
    return Triangle.newBuilder()
        .setColor(color)
        .setP1(p1)
        .setP2(p2)
        .setP3(p3)
        .build();
  }

  public static List<Vertex> getVertices(Triangle triangle) {
    return List.of(triangle.getP1(), triangle.getP2(), triangle.getP3());
  }
}
