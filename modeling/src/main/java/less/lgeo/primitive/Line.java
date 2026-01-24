package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;

import java.util.List;
import java.util.Optional;

public record Line(Color color, Point p1, Point p2) {

    public static final LineType type = LineType.LINE;

    public List<Point> getVertices() {
        return List.of(p1, p2);
    }

    public Line transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new Line(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2));
    }
}
