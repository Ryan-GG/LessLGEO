package less.lgeo.primitive;

public class Quadrilateral {

  private static final LineType type = LineType.QUADRILATERAL;

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
  private final double x4;
  private final double y4;
  private final double z4;

  public Quadrilateral(Color color, double x1, double y1, double z1, double x2, double y2,
      double z2, double x3,
      double y3,
      double z3, double x4, double y4, double z4) {
    // TODO add verification of ordering CCW or CW
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
    this.x4 = x4;
    this.y4 = y4;
    this.z4 = z4;
  }

  @Override
  public String toString() {
    Point p1 = new Point(x1, y1, z1);
    Point p2 = new Point(x2, y2, z2);
    Point p3 = new Point(x3, y3, z3);
    Point p4 = new Point(x4, y4, z4);
    return "Quadrilateral{" +
        "color=" + color +
        ", p1=" + p1 +
        ", p2=" + p2 +
        ", p3=" + p3 +
        ", p4=" + p4 +
        '}';
  }
}
