package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.getColor;
import static less.lgeo.common.VertexUtils.toVertex;

import java.util.List;
import java.util.Optional;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

public class TriangleUtils {

  public static Triangle toTriangle(int colorId, Vertex p1, Vertex p2, Vertex p3) {
    return Triangle.newBuilder()
        .setColorId(colorId)
        .setP1(p1)
        .setP2(p2)
        .setP3(p3)
        .build();
  }

  public static List<Vertex> getVertices(Triangle triangle) {
    return List.of(triangle.getP1(), triangle.getP2(), triangle.getP3());
  }

  public static Triangle transformTriangle(Triangle triangle,
      Optional<Matrix> transformationMatrix,
      Optional<Integer> inheritedColorId) {
    Vertex p1 = triangle.getP1();
    Vertex p2 = triangle.getP2();
    Vertex p3 = triangle.getP3();
    return toTriangle(
        getColor(inheritedColorId, triangle.getColorId()),
        transformationMatrix.map(value -> VertexUtils.transform(p1, value)).orElse(p1),
        transformationMatrix.map(value -> VertexUtils.transform(p2, value)).orElse(p2),
        transformationMatrix.map(value -> VertexUtils.transform(p3, value)).orElse(p3)
    );
  }

  public static Vertex getCentroid(Triangle triangle) {
    Vertex p1 = triangle.getP1();
    Vertex p2 = triangle.getP2();
    Vertex p3 = triangle.getP3();

    double xCentroid = (p1.getX() + p2.getX() + p3.getX()) / 3;
    double yCentroid = (p1.getY() + p2.getY() + p3.getY()) / 3;
    double zCentroid = (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

    return toVertex(xCentroid, yCentroid, zCentroid);
  }

}
