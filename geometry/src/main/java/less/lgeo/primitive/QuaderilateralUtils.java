package less.lgeo.primitive;

import java.util.List;

public class QuaderilateralUtils {


  public static Quadrilateral getQuadrilateral(Color color, Vertex p1, Vertex p2, Vertex p3,
      Vertex p4) {
    return Quadrilateral.newBuilder()
        .setColor(color)
        .setP1(p1)
        .setP2(p2)
        .setP3(p3)
        .setP4(p4)
        .build();
  }

  public static List<Vertex> getVerticies(Quadrilateral quadrilateral) {
    return List.of(quadrilateral.getP1(),
        quadrilateral.getP2(),
        quadrilateral.getP3(),
        quadrilateral.getP4());
  }

}
