package less.lgeo.primitive;

import java.util.List;

public class LineUtils {

  public static Line getLine(Color color, Vertex p1, Vertex p2) {
    return Line.newBuilder()
        .setColor(color)
        .setP1(p1)
        .setP2(p2)
        .build();
  }

  public static List<Vertex> getVerticies(Line line) {
    return List.of(line.getP1(), line.getP2());
  }
}
