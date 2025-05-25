package less.lgeo.primitive;

import java.util.HashSet;
import java.util.Set;

public class ModelUtils {

  /**
   * @param model gpb {@link Model}
   * @return All {@link Vertex} from the Parent Model
   */
  public static Set<Vertex> getVertices(Model model) {
    Set<Vertex> vertices = new HashSet<>();
    vertices.addAll(
        model.getLineList().stream().flatMap(line -> LineUtils.getVertices(line).stream())
            .toList());

    vertices.addAll(
        model.getTriangleList().stream().flatMap(line -> TriangleUtils.getVertices(line).stream())
            .toList());

    vertices.addAll(
        model.getQuadrilateralList().stream()
            .flatMap(line -> QuaderilateralUtils.getVertices(line).stream())
            .toList());

    vertices.addAll(
        model.getOptionalLineList().stream()
            .flatMap(line -> OptionalLineUtils.getVertices(line).stream())
            .toList());

    model.getPieceList()
        .forEach(subFileRef -> vertices.addAll(getVertices(subFileRef.getSubModel())));

    return vertices;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Line} from the Parent Model
   */
  public static Set<Line> getLines(Model model) {

    Set<Line> lines = new HashSet<>(model.getLineList());

    model.getPieceList()
        .forEach(subFileReference -> lines.addAll(getLines(subFileReference.getSubModel())));

    return lines;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Triangle} from the Parent Model
   */
  public static Set<Triangle> getTriangles(Model model) {

    Set<Triangle> triangles = new HashSet<>(model.getTriangleList());

    model.getPieceList()
        .forEach(
            subFileReference -> triangles.addAll(getTriangles(subFileReference.getSubModel())));

    return triangles;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Quadrilateral} from the Parent Model
   */
  public static Set<Quadrilateral> getQuadrilaterals(Model model) {

    Set<Quadrilateral> quadrilaterals = new HashSet<>(model.getQuadrilateralList());

    model.getPieceList()
        .forEach(
            subFileReference -> quadrilaterals.addAll(
                getQuadrilaterals(subFileReference.getSubModel())));

    return quadrilaterals;
  }


}
