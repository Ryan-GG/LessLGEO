package less.lgeo.primitive;

import less.lgeo.common.*;
import lombok.Data;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;

@Data
public class OptionalLine {

    private final LineType type = LineType.OPTIONAL_LINE;
    private final Color color;
    private final Vector3d p1;
    private final Vector3d p2;
    private final Vector3d p3;
    private final Vector3d p4;

    public OptionalLine(
            Color color,
            Vector3d p1,
            Vector3d p2,
            Vector3d p3,
            Vector3d p4) {
        this.color = color;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    /**
     * @return List of rendered line {@link Vector3d}, disregard control points.
     */
    public List<Vector3d> getVertices() {
        return List.of(p1, p2);
    }

    public OptionalLine transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new OptionalLine(
                CommonUtils.getColor(inheritedColor, color),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p2, matrix)).orElse(p2),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p3, matrix)).orElse(p3),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p4, matrix)).orElse(p4));
    }
}
