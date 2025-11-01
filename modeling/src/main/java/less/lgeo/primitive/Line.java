package less.lgeo.primitive;

import less.lgeo.common.*;
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
                CommonUtils.getColor(inheritedColor, color),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p2, matrix)).orElse(p2));
    }
}
