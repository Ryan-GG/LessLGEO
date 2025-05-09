package less.lgeo.primitive;

public class Line {

  private static final LineType type = LineType.LINE;

  private final Color color;
  private final double x1;
  private final double y1;
  private final double z1;
  private final double x2;
  private final double y2;
  private final double z2;

  public Line(Color color, double x1, double y1, double z1, double x2, double y2, double z2) {
    this.color = color;
    this.x1 = x1;
    this.y1 = y1;
    this.z1 = z1;
    this.x2 = x2;
    this.y2 = y2;
    this.z2 = z2;
  }

  @Override
  public String toString() {
    Point p1 = new Point(x1, y1, z1);
    Point p2 = new Point(x2, y2, z2);
    return "Line{" +
        "color=" + color +
        ", p1=" + p1 +
        ", p2=" + p2 +
        '}';
  }
}
