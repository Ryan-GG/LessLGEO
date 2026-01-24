package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;

import java.util.List;
import java.util.Optional;

public record Quadrilateral(Color color, Point p1, Point p2, Point p3, Point p4) {

    public static final LineType type = LineType.QUADRILATERAL;

    public List<Point> getVertices() {
        return List.of(p1, p2, p3, p4);
    }

    public Quadrilateral transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {

        return new Quadrilateral(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3),
                transformationMatrix.map(p4::transform).orElse(p4));
    }

    public List<Triangle> tessellate() {
        Triangle bottomLeft = new Triangle(color, p1, p2, p4);
        Triangle topRight = new Triangle(color, p2, p3, p4);
        return List.of(bottomLeft, topRight);
    }

}
