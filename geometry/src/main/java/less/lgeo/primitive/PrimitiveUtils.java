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
  public static LineType getLineType( int commandValue ) {
    switch ( commandValue ) {
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
   * / a d g x \
   * | b e h y |
   * | c f i z |
   * \ 0 0 0 1 /
   * @formatter:on
   */
  public static DMatrix4x4 gpbToDMatrix( Matrix matrix ) {
    return new DMatrix4x4(
        matrix.getA(), matrix.getD(), matrix.getG(), matrix.getX(),
        matrix.getB(), matrix.getE(), matrix.getH(), matrix.getY(),
        matrix.getC(), matrix.getF(), matrix.getI(), matrix.getZ(),
        0.0, 0.0, 0.0, matrix.getScale()
    );
  }

  public static Matrix dMatrixToGpb( DMatrix4x4 matrix ) {
    return Matrix.newBuilder()
        .setA( matrix.a11 )
        .setD( matrix.a12 )
        .setG( matrix.a13 )
        .setX( matrix.a14 )

        .setB( matrix.a21 )
        .setE( matrix.a22 )
        .setH( matrix.a23 )
        .setY( matrix.a24 )

        .setC( matrix.a31 )
        .setF( matrix.a32 )
        .setI( matrix.a33 )
        .setZ( matrix.a34 )

        .setScale( matrix.a44 )
        .build();

  }
}
