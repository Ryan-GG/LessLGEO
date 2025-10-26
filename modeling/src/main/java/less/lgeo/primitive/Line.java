package less.lgeo.primitive;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3Utils;
import lombok.Data;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;

@Data
public class Line {

    private final LineType type = LineType.LINE;
    private final Long id = null;
    private final int colorId;
    private final Vector3d p1;
    private final Vector3d p2;

    public Line(
            int colorId,
            Vector3d p1,
            Vector3d p2) {
        this.colorId = colorId;
        this.p1 = p1;
        this.p2 = p2;
    }

    public List<Vector3d> getVertices() {
        return List.of(p1, p2);
    }

    public Line transform(
            Optional<Matrix> transformationMatrix,
            Optional<Integer> inheritedColorId) {
        return new Line(
                getColor(inheritedColorId, getColorId()),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3Utils.transform(p2, matrix)).orElse(p2));
    }
}
