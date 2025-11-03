package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3dUtils;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;

public record Triangle(Color color, Vector3d p1, Vector3d p2, Vector3d p3) {

    public static final LineType type = LineType.TRIANGLE;

    public Vector3d getCentroid() {
        double xCentroid = (p1.x() + p2.x() + p3.x()) / 3;
        double yCentroid = (p1.y() + p2.y() + p3.y()) / 3;
        double zCentroid = (p1.z() + p2.z() + p3.z()) / 3;

        return new Vector3d(xCentroid, yCentroid, zCentroid);
    }

    public List<Vector3d> getVertices() {
        return List.of(p1, p2, p3);
    }

    public Triangle transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new Triangle(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p2, matrix)).orElse(p2),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p3, matrix)).orElse(p3));
    }

}
