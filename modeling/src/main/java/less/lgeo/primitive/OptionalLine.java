package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;

import java.util.Optional;

public record OptionalLine(Color color, Point p1, Point p2, Point p3, Point p4) {

    public static final LineType type = LineType.OPTIONAL_LINE;

    public OptionalLine transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new OptionalLine(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3),
                transformationMatrix.map(p4::transform).orElse(p4));
    }
}
