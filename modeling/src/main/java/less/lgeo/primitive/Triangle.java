
package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.getColor;

import java.util.List;
import java.util.Optional;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import lombok.Data;

@Data
public class Triangle {

  private final LineType type = LineType.TRIANGLE;
  private final Long id = null;
  private final int colorId;
  private final Vector3 p1;
  private final Vector3 p2;
  private final Vector3 p3;

  public Triangle(
      int colorId,
      Vector3 p1,
      Vector3 p2,
      Vector3 p3) {
    this.colorId = colorId;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
  }

  public List<Vector3> getVertices(Triangle triangle) {
    return List.of(triangle.getP1(), triangle.getP2(), triangle.getP3());
  }

  public Triangle transform(
      Optional<Matrix> transformationMatrix,
      Optional<Integer> inheritedColorId) {
    return new Triangle(
        getColor(inheritedColorId, colorId),
        transformationMatrix.map(value -> p1.transform(value)).orElse(p1),
        transformationMatrix.map(value -> p2.transform(value)).orElse(p2),
        transformationMatrix.map(value -> p3.transform(value)).orElse(p3));
  }

  public static Vector3 getCentroid(Triangle triangle) {
    Vector3 p1 = triangle.getP1();
    Vector3 p2 = triangle.getP2();
    Vector3 p3 = triangle.getP3();

    double xCentroid = (p1.getX() + p2.getX() + p3.getX()) / 3;
    double yCentroid = (p1.getY() + p2.getY() + p3.getY()) / 3;
    double zCentroid = (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

    return new Vector3(xCentroid, yCentroid, zCentroid);
  }

}
