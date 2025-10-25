
package less.lgeo.primitive;

import java.util.List;
import java.util.Optional;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import static less.lgeo.common.CommonUtils.getColor;
import lombok.Data;

@Data
public class OptionalLine {

  private final LineType type = LineType.OPTIONAL_LINE;
  private final Long id = null;
  private final int colorId;
  private final Vector3 p1;
  private final Vector3 p2;
  private final Vector3 p3;
  private final Vector3 p4;

  public OptionalLine(
      int colorId,
      Vector3 p1,
      Vector3 p2,
      Vector3 p3,
      Vector3 p4) {
    this.colorId = colorId;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
    this.p4 = p4;
  }

  /**
   * @return List of rendered line {@link Vector3}, disregard control points.
   */
  public List<Vector3> getVertices() {
    return List.of(p1, p2);
  }

  public OptionalLine transform(
      Optional<Matrix> transformationMatrix,
      Optional<Integer> inheritedColor) {
    return new OptionalLine(
        getColor(inheritedColor, colorId),
        transformationMatrix.map(value -> p1.transform(value)).orElse(p1),
        transformationMatrix.map(value -> p2.transform(value)).orElse(p2),
        transformationMatrix.map(value -> p3.transform(value)).orElse(p3),
        transformationMatrix.map(value -> p4.transform(value)).orElse(p4));
  }
}
