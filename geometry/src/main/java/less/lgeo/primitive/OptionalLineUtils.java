package less.lgeo.primitive;

import java.util.List;

public class OptionalLineUtils {


  public static OptionalLine getOptionalLine(Color color, Vertex p1, Vertex p2, Vertex p3,
      Vertex p4) {
    return OptionalLine.newBuilder()
        .setColor(color)
        .setP1(p1)
        .setP2(p2)
        .setP3(p3)
        .setP4(p4)
        .build();
  }

  /**
   * @return List of rendered line {@link Vertex}, disregard control points.
   */
  public static List<Vertex> getVertices(OptionalLine optionalLine) {
    return List.of(
        optionalLine.getP1(),
        optionalLine.getP2());
  }
}
