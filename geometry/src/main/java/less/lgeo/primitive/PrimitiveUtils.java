package less.lgeo.primitive;

public class PrimitiveUtils {


  /**
   * This weird switch statement is because GPB wants the unknown value as 0, cause thats the
   * default value assigned to the empty gpb
   *
   * @param commandValue
   * @return
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

  public static Vertex getPoint(double x, double y, double z) {
    return Vertex.newBuilder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

}
