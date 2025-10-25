
package less.lgeo.common;

public enum LineType {

  // TODO, do not match directly to line parsing
  LINE_TYPE_UNKNOWN,
  COMMENT_OR_META_CMD,
  SUB_FILE_REF,
  LINE,
  TRIANGLE,
  QUADRILATERAL,
  OPTIONAL_LINE;

  /**
   * TODO: Come back and verify if this is need or the gpb can be modified
   *
   * <p>
   * This weird switch statement is because GPB wants the unknown value as 0,
   * cause that's the
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
}
