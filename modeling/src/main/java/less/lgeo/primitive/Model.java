package less.lgeo.primitive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;

import less.lgeo.primitive.Line;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Triangle;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.common.Comment;
import less.lgeo.common.Matrix;
import less.lgeo.common.MetaCommand;
import less.lgeo.connection.Connection;
import static less.lgeo.common.Matrix.matrixToDMatrix;
import static less.lgeo.connection.ConnectionUtils.transformConnection;
import static less.lgeo.common.Matrix.dMatrixToMatrix;
import static less.lgeo.common.CommonUtils.getColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class Model {

  private final Long id;
  private final List<Comment> comments;
  private final List<MetaCommand> commands;
  private final List<Line> lines;
  private final List<Triangle> triangles;
  private final List<Quadrilateral> quadrilaterals;
  private final List<OptionalLine> optionalLines;
  private final List<SubFileReference> pieces;

  /**
   * @param model gpb {@link Model}
   * @return All {@link Quadrilateral} from the Parent Model
   */
  public Set<Quadrilateral> getQuadrilaterals() {

    Set<Quadrilateral> quadrilaterals = new HashSet<>(quadrilaterals);

    pieces.forEach(
        subFileReference -> quadrilaterals.addAll(subFileReference.getSubModel().getQuadrilaterals()));

    return quadrilaterals;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Connection} from the Parent Model
   */
  public Set<Connection> getConnections() {

    Set<Connection> connections = pieces.stream().map(SubFileReference::getPieceConnection)
        .filter(optionalConnection -> optionalConnection.isPresent())
        .map(connection -> connection.get()).collect(Collectors.toSet());

    pieces.forEach(
        subFileReference -> connections.addAll(subFileReference.getSubModel().getConnections()));

    return connections;
  }

  public Model transformModel() {
    return transformModel(Optional.empty(), Optional.empty());
  }

  private Model transformModel(
      Optional<Matrix> transformationMatrix,
      Optional<Integer> parentColor) {

    List<Line> transformedLines = getLines().stream().map(line -> line.transform(transformationMatrix, parentColor))
        .toList();

    List<Triangle> transformedTriangles = getTriangles().stream()
        .map(triangle -> triangle.transform(transformationMatrix, parentColor))
        .toList();

    List<Quadrilateral> transformedQuadrilaterals = getQuadrilaterals().stream()
        .map(quadrilateral -> quadrilateral.transform(transformationMatrix, parentColor))
        .toList();

    List<OptionalLine> transformedOptionalLines = getOptionalLines().stream()
        .map(optionaLine -> optionaLine.transform(transformationMatrix, parentColor))
        .toList();

    List<SubFileReference> transformedPieces = getPieces()
        .stream()
        .map(subFileReference -> {
          Matrix resulted = subFileReference.getMatrix();

          if (transformationMatrix.isPresent()) {
            DMatrix4x4 result = new DMatrix4x4();
            CommonOps_DDF4.mult(matrixToDMatrix(transformationMatrix.get()),
                matrixToDMatrix(subFileReference.getMatrix()),
                result);
            resulted = dMatrixToMatrix(result);
          }

          int subPartColorId = getColor(parentColor, subFileReference.getColorId());

          return new SubFileReference(
              subPartColorId,
              Matrix.IDENTITY_MATRIX,
              subFileReference.getSubModel().transformModel(Optional.of(resulted), Optional.of(subPartColorId)),
              subFileReference.getFileName(),
              transformConnection(subFileReference.getPieceConnection().get(), resulted));
        }).toList();

    return new Model(
        0,
        comments,
        commands,
        transformedLines,
        transformedTriangles,
        transformedQuadrilaterals,
        transformedOptionalLines,
        transformedPieces);
  }

  /**
   * Recursively tessellates a given LDraw Model
   *
   * @param model LDraw Model
   * @return a List triangles that comprise the passed in Model, Lines/Optional
   *         Lines are ignored as
   *         they cannot form a triangle
   */
  public List<Triangle> tesselate() {
    return tessellateModel(this);
  }

  private List<Triangle> tessellateModel(Model model) {

    List<Triangle> triangles = new ArrayList<>(model.getTriangles());

    List<Triangle> quadTriangles = model.getQuadrilaterals().stream()
        .flatMap(quadrilateral -> quadrilateral.tessellate().stream())
        .toList();

    triangles.addAll(quadTriangles);

    model.getPieces().forEach(subFileReference -> triangles.addAll(tessellateModel(subFileReference.getSubModel())));

    return triangles;

  }

}
