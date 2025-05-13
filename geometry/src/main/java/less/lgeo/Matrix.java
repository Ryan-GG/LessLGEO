package less.lgeo;

import less.lgeo.primitive.Vertex;

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
  private final double scale = 1.0;

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
  }


  public Vertex getOrigin() {
    return new Vertex(this.x, this.y, this.z);
  }


  @Override
  public String toString() {
    return String.format("""
            [ %f, %f, %f, 0
              %f, %f, %f, 0
              %f, %f, %f, 0
              %f, %f, %f, %f ]""",
        this.a,
        this.d,
        this.g,
        this.b,
        this.e,
        this.h,
        this.c,
        this.f,
        this.i,
        this.x,
        this.y,
        this.z,
        this.scale);
  }
}
