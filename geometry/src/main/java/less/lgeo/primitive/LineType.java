package less.lgeo.primitive;

public enum LineType {
  COMMENT_OR_META_CMD(0),
  SUB_FILE_REF(1),
  LINE(2),
  TRIANGLE(3),
  QUADRILATERAL(4),
  OPTIONAL_LINE(5),
  INVALID(6);

  private final double type;

  LineType(double type) {
    this.type = type;
  }

  // Static method to convert double to enum
  public static LineType fromInteger(double ordinalValue) {
    for (LineType type : LineType.values()) {
      if (type.getType() == ordinalValue) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid Status code: " + ordinalValue);
  }

  public double getType() {
    return type;
  }

  @Override
  public String toString() {
    return this.name() + " (" + this.type + ")";
  }
}
