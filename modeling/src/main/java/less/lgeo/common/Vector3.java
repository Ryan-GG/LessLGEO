package less.lgeo.common;

import org.joml.Vector3d;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector3 {

  private final double x;
  private final double y;
  private final double z;

  public static Vector3 toVector3(Vector3d vector3d) {
    return new Vector3(vector3d.x(), vector3d.y(), vector3d.z());
  }

  public static Vector3d toVector3d(Vector3 vertex) {
    return new Vector3d(vertex.getX(), vertex.getY(), vertex.getZ());
  }

  /**
   *
   * u' = a*u + b*v + c*w + x
   * v' = d*u + e*v + f*w + y
   * w' = g*u + h*v + i*w + z
   * 
   * @param matrix Matrix that holds transformation
   * @return The resulting {@link Vector3} from the previous position with the
   *         transformation
   *         applied.
   */
  public Vector3 transform(Matrix matrix) {
    double newX = matrix.getA() * getX() + matrix.getB() * getY()
        + matrix.getC() * getZ() + matrix.getX();
    double newY = matrix.getD() * getX() + matrix.getE() * getY()
        + matrix.getF() * getZ() + matrix.getY();
    double newZ = matrix.getG() * getX() + matrix.getH() * getY()
        + matrix.getI() * getZ() + matrix.getZ();

    return new Vector3(newX, newY, newZ);
  }
}
