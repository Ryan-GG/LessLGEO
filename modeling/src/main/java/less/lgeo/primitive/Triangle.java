package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;

import java.util.List;
import java.util.Optional;

public record Triangle(Color color, Point p1, Point p2, Point p3) {

    public static final LineType type = LineType.TRIANGLE;

    public Point getCentroid() {
        double xCentroid = (p1.x() + p2.x() + p3.x()) / 3;
        double yCentroid = (p1.y() + p2.y() + p3.y()) / 3;
        double zCentroid = (p1.z() + p2.z() + p3.z()) / 3;

        return Point.of(xCentroid, yCentroid, zCentroid);
    }

    public List<Point> getVertices() {
        return List.of(p1, p2, p3);
    }

    public Triangle transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new Triangle(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3));
    }

}
