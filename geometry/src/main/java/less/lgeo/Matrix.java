package less.lgeo;

import less.lgeo.primitive.Point;

// @formatter:off
/**
 * This represents the rotation and scaling of the part. The entire 4x4 3D transformation matrix would then
 * take either of the following forms:
 * / a d g 0 \   / a b c x \
 * | b e h 0 |   | d e f y |
 * | c f i 0 |   | g h i z |
 * \ x y z 1 /   \ 0 0 0 1 /
 * The above two forms are essentially equivalent, but note the location of the transformation portion (x, y, z)
 * relative to the other terms.
 * Formally, the transformed point (u', v', w') can be calculated from point (u, v, w) as follows:
 * u' = a*u + b*v + c*w + x
 * v' = d*u + e*v + f*w + y
 * w' = g*u + h*v + i*w + z
 */
// @formatter:on
public class Matrix {

  private final double x;
  private final double y;
  private final double z;
  private final double a;
  private final double b;
  private final double c;
  private final double d;
  private final double e;
  private final double f;
  private final double g;
  private final double h;
  private final double i;
  private final double[][] matrix = new double[4][4];

  /**
   * TODO I'm choosing this format, no idea if its right
   *  / a d g 0 \
   *  | b e h 0 |
   *  | c f i 0 |
   *  \ x y z 1 /
   */
  public Matrix(double x, double y, double z, double a, double b, double c, double d, double e,
      double f, double g,
      double h, double i) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
    this.f = f;
    this.g = g;
    this.h = h;
    this.i = i;
    setMatrix();
  }

  private void setMatrix() {
    matrix[0][0] = this.a;
    matrix[1][0] = this.b;
    matrix[2][0] = this.c;
    matrix[3][0] = this.x;
    matrix[0][1] = this.d;
    matrix[1][1] = this.e;
    matrix[2][1] = this.f;
    matrix[3][1] = this.y;
    matrix[0][2] = this.g;
    matrix[1][2] = this.h;
    matrix[2][2] = this.i;
    matrix[3][2] = this.z;
    matrix[0][3] = 0.0; // x rotation
    matrix[1][3] = 0.0; // y rotation
    matrix[2][3] = 0.0; // z rotation
    matrix[3][3] = 1;   // scaling
  }

  public Point getOrigin() {
    return new Point(this.x, this.y, this.z);
  }

  public void rotateTo(double xRotation, double yRotation, double zRotation) {
    matrix[0][3] = xRotation;
    matrix[1][3] = yRotation;
    matrix[2][3] = zRotation;
  }

  public void setXRotation(double rotation) {
    matrix[0][3] = rotation;
  }

  public void setYRotation(double rotation) {
    matrix[1][3] = rotation;
  }

  public void setZRotation(double rotation) {
    matrix[2][3] = rotation;
  }

  public void setScaling(double scale) {
    matrix[3][3] = scale;
  }


}
