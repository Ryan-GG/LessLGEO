package less.lgeo.primitive;

import static less.lgeo.primitive.LineUtils.transformLine;
import static less.lgeo.primitive.OptionalLineUtils.transformOptionalLine;
import static less.lgeo.primitive.PrimitiveUtils.dMatrixToGpb;
import static less.lgeo.primitive.PrimitiveUtils.gpbToDMatrix;
import static less.lgeo.primitive.QuaderilateralUtils.transformQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.transformTriangle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelUtils {

  public static final Matrix IDENTITY_MATRIX = Matrix.newBuilder()
      .setA(1)
      .setE(1)
      .setI(1)
      .setScale(1)
      .build();

  private static final Logger logger = LoggerFactory.getLogger(ModelUtils.class);

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

  /**
   * @param model gpb {@link Model}
   * @return All {@link OptionalLine} from the Parent Model
   */
  public static Set<OptionalLine> getOptionalLines(Model model) {

    Set<OptionalLine> optionalLines = new HashSet<>(model.getOptionalLineList());

    model.getPieceList()
        .forEach(
            subFileReference -> optionalLines.addAll(
                getOptionalLines(subFileReference.getSubModel())));

    return optionalLines;
  }

  public static Model transformModel(Model model) {
    return transformModel(model, Optional.empty()
    );
  }

  private static Model transformModel(Model model, Optional<Matrix> transformationMatrix) {

    logger.info("transformation matrix: {}",
        transformationMatrix.isPresent() ? transformationMatrix.get() : "empty");
    List<Line> transformedLines =
        model.getLineList().stream().map(line -> transformLine(line, transformationMatrix))
            .toList();

    List<Triangle> transformedTriangles =
        model.getTriangleList().stream()
            .map(triangle -> transformTriangle(triangle, transformationMatrix))
            .toList();

    List<Quadrilateral> transformedQuadrilaterals =
        model.getQuadrilateralList().stream()
            .map(quadrilateral -> transformQuadrilateral(quadrilateral, transformationMatrix))
            .toList();

    List<OptionalLine> transformedOptionalLines =
        model.getOptionalLineList().stream()
            .map(optionalLine -> transformOptionalLine(optionalLine, transformationMatrix))
            .toList();

    List<SubFileReference> transformedPieces =
        model.getPieceList()
            .stream()
            .map(subFileReference -> {
              // Prepare output matrix
              Matrix resulted = subFileReference.getMatrix();

              if (transformationMatrix.isPresent()) {
                DMatrix4x4 result = new DMatrix4x4();
                CommonOps_DDF4.mult(gpbToDMatrix(transformationMatrix.get()),
                    gpbToDMatrix(subFileReference.getMatrix()),
                    result);
                resulted = dMatrixToGpb(result);
              }

              return SubFileReference.newBuilder()
                  .setName(subFileReference.getName())
                  .setColor(subFileReference.getColor())
                  .setMatrix(IDENTITY_MATRIX)
                  .setSubModel(
                      transformModel(subFileReference.getSubModel(),
                          Optional.of(resulted)))
                  .build();
            })
            .toList();

    return Model.newBuilder()
        .addAllComment(model.getCommentList())
        .addAllCommand(model.getCommandList())
        .addAllLine(transformedLines)
        .addAllTriangle(transformedTriangles)
        .addAllQuadrilateral(transformedQuadrilaterals)
        .addAllOptionalLine(transformedOptionalLines)
        .addAllPiece(transformedPieces)
        .build();
  }

}
