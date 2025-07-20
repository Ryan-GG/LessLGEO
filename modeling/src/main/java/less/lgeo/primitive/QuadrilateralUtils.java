package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.getColor;

import java.util.List;
import java.util.Optional;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

public class QuadrilateralUtils {


  public static Quadrilateral toQuadrilateral(int colorId, Vertex p1, Vertex p2, Vertex p3,
      Vertex p4) {
    return Quadrilateral.newBuilder()
        .setColorId(colorId)
        .setP1(p1)
        .setP2(p2)
        .setP3(p3)
        .setP4(p4)
        .build();
  }

  public static List<Vertex> getVertices(Quadrilateral quadrilateral) {
    return List.of(quadrilateral.getP1(),
        quadrilateral.getP2(),
        quadrilateral.getP3(),
        quadrilateral.getP4());
  }

  public static Quadrilateral transformQuadrilateral(Quadrilateral quadrilateral,
      Optional<Matrix> transformationMatrix,
      Optional<Integer> inheritedColorId) {
    Vertex p1 = quadrilateral.getP1();
    Vertex p2 = quadrilateral.getP2();
    Vertex p3 = quadrilateral.getP3();
    Vertex p4 = quadrilateral.getP4();

    return toQuadrilateral(
        getColor(inheritedColorId, quadrilateral.getColorId()),
        transformationMatrix.map(value -> VertexUtils.transform(p1, value)).orElse(p1),
        transformationMatrix.map(value -> VertexUtils.transform(p2, value)).orElse(p2),
        transformationMatrix.map(value -> VertexUtils.transform(p3, value)).orElse(p3),
        transformationMatrix.map(value -> VertexUtils.transform(p4, value)).orElse(p4)
    );
  }

}
