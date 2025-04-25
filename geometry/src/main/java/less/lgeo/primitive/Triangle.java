package less.lgeo.primitive;

public class Triangle {

  private static final LineType type = LineType.TRIANGLE;

  private final Color color;
  private final double x1;
  private final double y1;
  private final double z1;
  private final double x2;
  private final double y2;
  private final double z2;
  private final double x3;
  private final double y3;
  private final double z3;

  public Triangle(Color color, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
      double z3) {
    this.color = color;
    this.x1 = x1;
    this.y1 = y1;
    this.z1 = z1;
    this.x2 = x2;
    this.y2 = y2;
    this.z2 = z2;
    this.x3 = x3;
    this.y3 = y3;
    this.z3 = z3;
  }
}
