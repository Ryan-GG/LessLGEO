
package less.lgeo.common;

import org.ejml.data.DMatrix4x4;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * / a b c x \
 * | d e f y |
 * | g h i z |
 * \ 0 0 0 scale /
 */
@Data
@Builder
@AllArgsConstructor
public class Matrix {

  public static final Matrix IDENTITY_MATRIX = Matrix.builder()
      .a(1)
      .e(1)
      .i(1)
      .scale(1)
      .build();

  private final double a;
  private final double b;
  private final double c;
  private final double x;

  private final double d;
  private final double e;
  private final double f;
  private final double y;

  private final double g;
  private final double h;
  private final double i;
  private final double z;

  private final double scale;

  /**
   * @formatter:off
   * / a b c x \
   * | d e f y |
   * | g h i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  public static DMatrix4x4 matrixToDMatrix(Matrix matrix) {
    return new DMatrix4x4(
        matrix.getA(), matrix.getB(), matrix.getC(), matrix.getX(),
        matrix.getD(), matrix.getE(), matrix.getF(), matrix.getY(),
        matrix.getG(), matrix.getH(), matrix.getI(), matrix.getZ(),
        0.0, 0.0, 0.0, matrix.getScale());
  }

  public static Matrix dMatrixToMatrix(DMatrix4x4 matrix) {
    return new Matrix(
        matrix.a11, matrix.a12, matrix.a13, matrix.a14,
        matrix.a21, matrix.a22, matrix.a23, matrix.a24,
        matrix.a31, matrix.a32, matrix.a33, matrix.a34,
        matrix.a44);
  }

  /**
   * @formatter:off
   * / a b c x \
   * | d e f y |
   * | g h i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  @Override
  public String toString() {
    return String.format(
        """
            \n
            / %.4f %.4f %.4f %.4f\\
            | %.4f %.4f %.4f %.4f |
            | %.4f %.4f %.4f %.4f |
            \\ 0    0    0   %.4f /
            """,
        getA(), getB(), getC(), getX(),
        getD(), getE(), getF(), getY(),
        getG(), getH(), getI(), getZ(), getScale());
  }
}
