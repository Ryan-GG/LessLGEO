package less.lgeo.primitive;

import org.ejml.data.DMatrix4x4;

public class PrimitiveUtils {


  /**
   * TODO: Come back and verify if this is need or the gpb can be modified
   *
   * <p>
   * This weird switch statement is because GPB wants the unknown value as 0, cause that's the
   * default value assigned to the empty gpb
   * </p>
   *
   * @param commandValue Line's Command Value
   * @return Returns the corrected LineType Enum based on the scene line value
   */
  public static LineType getLineType(int commandValue) {
    switch (commandValue) {
      case 0 -> {
        return LineType.COMMENT_OR_META_CMD;
      }
      case 1 -> {
        return LineType.SUB_FILE_REF;
      }
      case 2 -> {
        return LineType.LINE;
      }
      case 3 -> {
        return LineType.TRIANGLE;
      }
      case 4 -> {
        return LineType.QUADRILATERAL;
      }
      case 5 -> {
        return LineType.OPTIONAL_LINE;
      }
      default -> {
        return LineType.LINE_TYPE_UNKNOWN;
      }
    }
  }

  /**
   * @formatter:off
   * / a b c x \
   * | d e f y |
   * | g h i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  public static DMatrix4x4 gpbToDMatrix(Matrix matrix) {
    return new DMatrix4x4(
        matrix.getA(), matrix.getB(), matrix.getC(), matrix.getX(),
        matrix.getD(), matrix.getE(), matrix.getF(), matrix.getY(),
        matrix.getG(), matrix.getH(), matrix.getI(), matrix.getZ(),
        0.0, 0.0, 0.0, matrix.getScale()
    );
  }

  public static Matrix dMatrixToGpb(DMatrix4x4 matrix) {
    return Matrix.newBuilder()
        .setA(matrix.a11)
        .setB(matrix.a12)
        .setC(matrix.a13)
        .setX(matrix.a14)

        .setD(matrix.a21)
        .setE(matrix.a22)
        .setF(matrix.a23)
        .setY(matrix.a24)

        .setG(matrix.a31)
        .setH(matrix.a32)
        .setI(matrix.a33)
        .setZ(matrix.a34)

        .setScale(matrix.a44)
        .build();

  }
}
