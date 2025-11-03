package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3dUtils;
import lombok.Data;
import org.joml.Vector3d;

import java.util.Optional;

@Data
public class OptionalLine {

    public final LineType type = LineType.OPTIONAL_LINE;
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

    public OptionalLine transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new OptionalLine(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p1, matrix)).orElse(p1),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p2, matrix)).orElse(p2),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p3, matrix)).orElse(p3),
                transformationMatrix.map(matrix -> Vector3dUtils.transform(p4, matrix)).orElse(p4));
    }
}
