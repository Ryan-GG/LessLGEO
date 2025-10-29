package less.lgeo.primitive;

import less.lgeo.common.Comment;
import less.lgeo.common.Matrix;
import less.lgeo.common.MetaCommand;
import less.lgeo.connection.Connection;
import lombok.Data;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;
import static less.lgeo.common.Matrix.dMatrixToMatrix;
import static less.lgeo.common.Matrix.matrixToDMatrix;

@Data
public class Model {

    private final Long id = null;
    private final List<Comment> comments;
    private final List<MetaCommand> commands;
    private final List<Line> lines;
    private final List<Triangle> triangles;
    private final List<Quadrilateral> quadrilaterals;
    private final List<OptionalLine> optionalLines;
    private final List<SubFileReference> pieces;

    public Model(
            List<Comment> comments,
            List<MetaCommand> commands,
            List<Line> lines,
            List<Triangle> triangles,
            List<Quadrilateral> quadrilaterals,
            List<OptionalLine> optionalLines,
            List<SubFileReference> pieces) {
        this.comments = comments;
        this.commands = commands;
        this.lines = lines;
        this.triangles = triangles;
        this.quadrilaterals = quadrilaterals;
        this.optionalLines = optionalLines;
        this.pieces = pieces;
    }

    /**
     * @return All {@link Quadrilateral} from the Parent Model
     */
    public List<Quadrilateral> getQuadrilaterals() {

        List<Quadrilateral> quadrilaterals = new ArrayList<>(this.quadrilaterals);

        pieces.forEach(
                subFileReference -> quadrilaterals.addAll(subFileReference.getSubModel().getQuadrilaterals()));

        return quadrilaterals;
    }

    /**
     * @return All {@link Connection} from the Parent Model
     */
    public List<Connection> getConnections() {

        List<Connection> connections = new ArrayList<>(pieces.stream().map(SubFileReference::getPieceConnection)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());

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
                            //FIXME
                            Optional.of(subFileReference.getPieceConnection().get().transformConnection(resulted)));
                }).toList();

        return new Model(
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
     * @return a List triangles that comprise the passed in Model, Lines/Optional
     * Lines are ignored as
     * they cannot form a triangle
     */
    public List<Triangle> tessellate() {
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
