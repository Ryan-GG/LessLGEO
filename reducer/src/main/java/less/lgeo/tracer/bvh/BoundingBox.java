package less.lgeo.tracer.bvh;

import less.lgeo.primitive.Point;
import less.lgeo.primitive.Triangle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;

import java.util.List;

/**
 * Represents a 3D axis-aligned box defined by its 8 vertices.
 * A bounding box has no reference to LDraw Orientation, just numerically gets min/max
 * <p>
 * Coordinate system:
 * <ul>
 *   <li>+X → right</li>
 *   <li>−Y → up</li>
 *   <li>+Z → forward (toward H)</li>
 * </ul>
 * <p>
 * Vector3 layout:
 *
 * <pre>
 *            (-Y up)
 *               ↑
 *               │
 *               │      D────────C
 *               │     ╱│       ╱│
 *               │    A────────B │
 *               │    │ │      │ │
 *               │    │ H──────│ G
 *               │    │╱       │╱
 *               │    E────────F
 *               └────────────────────→ +X
 *                        ( +Z → forward, toward H )
 * </pre>
 * Faces:
 * <p>
 *     Ordering is CCW(Counter-Clockwise) starting from the bottom left vertex respective to the face's normal
 * </p>
 * <ul>
 *   <li>Top (up): A–B–C–D (−Y)</li>
 *   <li>Bottom (down): E–F–G–H (+Y)</li>
 *   <li>Front (facing −Z): E-F–B–A</li>
 *   <li>Back (facing +Z): G-H-D-C</li>
 *   <li>Left (−X): H–E–A–D</li>
 *   <li>Right (+X): F–G–C–B</li>
 * </ul>
 * <p>
 * This vertex labeling follows a conventional box layout where:
 * <ul>
 *   <li>–Y is up</li>
 *   <li>+Z extends forward</li>
 *   <li>+X extends to the right</li>
 * </ul>
 */
@Getter
@NoArgsConstructor
public class BoundingBox {

    private final Point min = Point.of(Double.POSITIVE_INFINITY);
    private final Point max = Point.of(Double.NEGATIVE_INFINITY);
    private Vector3d size = new Vector3d(Double.POSITIVE_INFINITY);

    public BoundingBox(List<Point> vertices) {
        vertices.forEach(this::growToInclude);
    }


    private void setMin(Point point) {
        min.value().min(point.value());
    }

    private void setMax(Point point) {
        max.value().max(point.value());
    }

    public void growToInclude(Point point) {
        setMin(point);
        setMax(point);
        size = new Vector3d(Math.abs(max.x() - min.x()), Math.abs(max.y() - min.y()), Math.abs(max.z() - min.z()));
    }

    public void growToInclude(Triangle triangle) {
        growToInclude(triangle.p1());
        growToInclude(triangle.p2());
        growToInclude(triangle.p3());
    }

    public boolean includesPoint(Point point) {
        boolean inXBounds = min.x() <= point.x() && point.x() <= max.x();
        boolean inYBounds = min.y() <= point.y() && point.y() <= max.y();
        boolean inZBounds = min.z() <= point.z() && point.z() <= max.z();

        return inXBounds && inYBounds && inZBounds;
    }

    public Point getCenter() {
        Vector3d min = this.min.value();
        Vector3d max = this.max.value();
        Vector3d center = min.add(max, new Vector3d()).mul(0.5);
        return new Point(center);
    }

    @Override
    public String toString() {
        return String.format("Min: %s, Center: %s, Max: %s",
                getMin(),
                getCenter(),
                getMax()
        );
    }


}
