package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.Comment;
import less.lgeo.common.Matrix;
import less.lgeo.common.MetaCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record Model(
        //FIXME, comments/commands need to be stored in the database
        List<Comment> comments,
        List<MetaCommand> commands,
        List<Line> lines,
        List<Triangle> triangles,
        List<Quadrilateral> quadrilaterals,
        List<OptionalLine> optionalLines,
        List<SubFileReference> pieces) {

    /**
     * @return All {@link Quadrilateral}s and child quadrilaterals for the given {@link Model}
     */
    private List<Quadrilateral> getAllQuadrilaterals() {

        List<Quadrilateral> quadrilaterals = new ArrayList<>(this.quadrilaterals);

        pieces.forEach(
                subFileReference -> quadrilaterals.addAll(subFileReference.subModel().getAllQuadrilaterals()));

        return quadrilaterals;
    }

    /**
     * @return All {@link Triangle}s and child triangles for the given {@link Model}
     */
    private List<Triangle> getAllTriangles() {

        List<Triangle> triangleList = new ArrayList<>(this.triangles);

        pieces.forEach(
                subFileReference -> triangleList.addAll(subFileReference.subModel().getAllTriangles()));

        return triangleList;
    }

    public Model transformModel() {
        return transformModel(Optional.empty(), Optional.empty());
    }

    Model transformModel(
            Optional<Matrix> transformationMatrix,
            Optional<Color> parentColor) {

        List<Line> transformedLines = lines().stream().map(line -> line.transform(transformationMatrix, parentColor))
                .toList();

        List<Triangle> transformedTriangles = triangles().stream()
                .map(triangle -> triangle.transform(transformationMatrix, parentColor))
                .toList();

        List<Quadrilateral> transformedQuadrilaterals = quadrilaterals().stream()
                .map(quadrilateral -> quadrilateral.transform(transformationMatrix, parentColor))
                .toList();

        List<OptionalLine> transformedOptionalLines = optionalLines().stream()
                .map(optionalLine -> optionalLine.transform(transformationMatrix, parentColor))
                .toList();

        List<SubFileReference> transformedPieces = pieces().stream()
                .map(subFileReference -> subFileReference.transform(transformationMatrix, parentColor))
                .toList();

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
        List<Triangle> triangles = new ArrayList<>(getAllTriangles());

        List<Triangle> quadTriangles = getAllQuadrilaterals().stream()
                .flatMap(quadrilateral -> quadrilateral.tessellate().stream())
                .toList();

        triangles.addAll(quadTriangles);
        
        return triangles;

    }

}
