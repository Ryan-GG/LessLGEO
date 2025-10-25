
package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.getColor;

import java.util.List;
import java.util.Optional;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import lombok.Data;

@Data
public class Line {

  private final LineType type = LineType.LINE;
  private final Long id = null;
  private final int colorId;
  private final Vector3 p1;
  private final Vector3 p2;

  public Line(
      int colorId,
      Vector3 p1,
      Vector3 p2) {
    this.colorId = colorId;
    this.p1 = p1;
    this.p2 = p2;
  }

  public static List<Vector3> getVertices(Line line) {
    return List.of(line.getP1(), line.getP2());
  }

  public Line transform(
      Optional<Matrix> transformationMatrix,
      Optional<Integer> inheritedColorId) {
    return new Line(
        getColor(inheritedColorId, getColorId()),
        transformationMatrix.map(value -> getP1().transform(value)).orElse(getP1()),
        transformationMatrix.map(value -> getP2().transform(value)).orElse(getP2()));
  }
}
