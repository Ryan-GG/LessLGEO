package less.lgeo.primitive;

import java.util.HashSet;
import java.util.Set;

public class ModelUtils {

  public static Set<Vertex> getVerticies(Model model) {
    Set<Vertex> vertices = new HashSet<>();
    vertices.addAll(
        model.getLineList().stream().flatMap(line -> LineUtils.getVerticies(line).stream())
            .toList());

    vertices.addAll(
        model.getTriangleList().stream().flatMap(line -> TriangleUtils.getVerticies(line).stream())
            .toList());

    vertices.addAll(
        model.getQuadrilateralList().stream()
            .flatMap(line -> QuaderilateralUtils.getVerticies(line).stream())
            .toList());

    vertices.addAll(
        model.getOptionalLineList().stream()
            .flatMap(line -> OptionalLineUtils.getVerticies(line).stream())
            .toList());

    model.getPieceList().stream()
        .forEach(subFileRef -> getVerticiesHelper(subFileRef.getSubModel(), vertices));
    return vertices;
  }

  private static void getVerticiesHelper(Model model, Set<Vertex> vertices) {

    vertices.addAll(getVerticies(model));
  }

}
