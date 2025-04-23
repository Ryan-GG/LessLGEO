package less.lgeo.primitive;

public class Triangle {

  private static final LineType type = LineType.TRIANGLE;

  private final Color color;
  private final int x1;
  private final int y1;
  private final int z1;
  private final int x2;
  private final int y2;
  private final int z2;
  private final int x3;
  private final int y3;
  private final int z3;

  public Triangle(Color color, int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3,
      int z3) {
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
