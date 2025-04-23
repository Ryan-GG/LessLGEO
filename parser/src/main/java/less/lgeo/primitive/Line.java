package less.lgeo.primitive;

public class Line {

  private static final LineType type = LineType.LINE;

  private final Color color;
  private final int x1;
  private final int y1;
  private final int z1;
  private final int x2;
  private final int y2;
  private final int z2;

  public Line(Color color, int x1, int y1, int z1, int x2, int y2, int z2) {
    this.color = color;
    this.x1 = x1;
    this.y1 = y1;
    this.z1 = z1;
    this.x2 = x2;
    this.y2 = y2;
    this.z2 = z2;
  }
}
