package less.lgeo.primitive;

import java.util.HashSet;
import java.util.Set;

public class ModelUtils {

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

  public static Set<Line> getLines(Model model) {

    Set<Line> lines = new HashSet<>(model.getLineList());

    model.getPieceList()
        .forEach(subFileReference -> lines.addAll(getLines(subFileReference.getSubModel())));

    return lines;
  }


}
