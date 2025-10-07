package less.lgeo.common;

import org.joml.Vector3d;

public class VertexUtils {

  public static Vertex toVertex(double x, double y, double z) {
    return Vertex.newBuilder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public static Vector3d toVector3d(Vertex vertex) {
    return new Vector3d(vertex.getX(), vertex.getY(), vertex.getZ());
  }

  /**
   * @formatter:off
   * u' = a*u + b*v + c*w + x
   * v' = d*u + e*v + f*w + y
   * w' = g*u + h*v + i*w + z
   * @formatter:on
   *
   * @param oldVertex point location
   * @param matrix    Matrix that holds transformation
   * @return The resulting {@link Vertex} from the previous position with the transformation
   * applied.
   */
  public static Vertex transform(Vertex oldVertex, Matrix matrix) {
    double newX = matrix.getA() * oldVertex.getX() + matrix.getB() * oldVertex.getY()
        + matrix.getC() * oldVertex.getZ() + matrix.getX();
    double newY = matrix.getD() * oldVertex.getX() + matrix.getE() * oldVertex.getY()
        + matrix.getF() * oldVertex.getZ() + matrix.getY();
    double newZ = matrix.getG() * oldVertex.getX() + matrix.getH() * oldVertex.getY()
        + matrix.getI() * oldVertex.getZ() + matrix.getZ();

    return toVertex(newX, newY, newZ);
  }
}
