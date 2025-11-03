package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3dUtils;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;

public record Line(Color color, Vector3d p1, Vector3d p2) {

    public static final LineType type = LineType.LINE;

    public List<Vector3d> getVertices() {
        return List.of(p1, p2);
    }

    public Line transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new Line(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p2, matrix)).orElse(p2));
    }
}
