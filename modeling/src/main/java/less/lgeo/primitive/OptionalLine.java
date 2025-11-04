package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3dUtils;
import org.joml.Vector3d;

import java.util.Optional;

public record OptionalLine(Color color, Vector3d p1, Vector3d p2, Vector3d p3, Vector3d p4) {

    public static final LineType type = LineType.OPTIONAL_LINE;
    
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
