package less.lgeo.tracer;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Triangle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;

import java.util.List;
import java.util.stream.Stream;

import static less.lgeo.common.VertexUtils.toVector3d;
import static less.lgeo.common.VertexUtils.toVertex;
import static less.lgeo.primitive.LineUtils.toLine;
import static less.lgeo.test.ModelTestUtils.UNKNOWN_COLOR_ID;


/**
 * Represents a 3D axis-aligned box defined by its 8 vertices.
 * <p>
 * Coordinate system:
 * <ul>
 *   <li>+X → right</li>
 *   <li>−Y → up</li>
 *   <li>+Z → forward (toward H)</li>
 * </ul>
 * <p>
 * Vertex layout:
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

    private Vector3d min = new Vector3d(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    private Vector3d max = new Vector3d(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

    public BoundingBox(List<Vertex> vertices) {
        vertices.forEach(this::growToInclude);
    }

    private Vector3d calculateA() {
        return new Vector3d(min.x, max.y, min.z);
    }

    private Vector3d calculateB() {
        return new Vector3d(max.x, max.y, min.z);
    }

    private Vector3d calculateC() {
        return max;
    }

    private Vector3d calculateD() {
        return new Vector3d(min.x, max.y, max.z);
    }

    private Vector3d calculateE() {
        return min;
    }

    private Vector3d calculateF() {
        return new Vector3d(max.x, min.y, min.z);
    }

    private Vector3d calculateG() {
        return new Vector3d(max.x, min.y, max.z);
    }

    private Vector3d calculateH() {
        return new Vector3d(min.x(), min.y, max.z);
    }

    public void growToInclude(Vertex point) {
        growToInclude(toVector3d(point));
    }

    public void growToInclude(Vector3d point) {
        min = min(min, point);
        max = max(max, point);
    }

    private Vector3d min(Vector3d a, Vector3d b) {
        double xMin = Math.min(a.x, b.x);
        double yMin = Math.max(a.y, b.y);
        double zMin = Math.min(a.z, b.z);
        return new Vector3d(xMin, yMin, zMin);
    }

    private Vector3d max(Vector3d a, Vector3d b) {
        double xMax = Math.max(a.x, b.x);
        double yMax = Math.min(a.y, b.y);
        double zMax = Math.max(a.z, b.z);
        return new Vector3d(xMax, yMax, zMax);
    }

    public void growToInclude(Triangle triangle) {
        growToInclude(triangle.getP1());
        growToInclude(triangle.getP2());
        growToInclude(triangle.getP3());
    }

    public boolean includesPoint(Vector3d point) {
        boolean inXBounds = min.x <= point.x && point.x <= max.x;
        // Flipped due to -Y being UP
        boolean inYBounds = max.y <= point.y && point.y <= min.y;
        boolean inZBounds = min.z <= point.z && point.z <= max.z;

        return inXBounds && inYBounds && inZBounds;
    }

    public boolean includesPoint(Vertex point) {
        return includesPoint(toVector3d(point));
    }

    public Vector3d getCenter() {
        return (getMin().add(getMax())).mul(0.5);
    }

    public List<Line> getBoundingBoxAsLines() {

        return Stream.of(
                        getTopFace(),
                        getBottomFace(),
                        getFrontFace(),
                        getBackFace(),
                        getLeftFace(),
                        getRightFace())
                .flatMap(List::stream)
                .toList();

    }

    private List<Line> getTopFace() {
        Vector3d a = calculateA();
        Vector3d b = calculateB();
        Vector3d c = calculateC();
        Vector3d d = calculateD();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(a), toVertex(b)),
                toLine(UNKNOWN_COLOR_ID, toVertex(b), toVertex(c)),
                toLine(UNKNOWN_COLOR_ID, toVertex(c), toVertex(d)),
                toLine(UNKNOWN_COLOR_ID, toVertex(d), toVertex(a))
        );
    }

    private List<Line> getBottomFace() {
        Vector3d e = calculateE();
        Vector3d f = calculateF();
        Vector3d g = calculateG();
        Vector3d h = calculateH();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(e), toVertex(f)),
                toLine(UNKNOWN_COLOR_ID, toVertex(f), toVertex(g)),
                toLine(UNKNOWN_COLOR_ID, toVertex(g), toVertex(h)),
                toLine(UNKNOWN_COLOR_ID, toVertex(h), toVertex(e))
        );
    }

    private List<Line> getFrontFace() {
        Vector3d e = calculateE();
        Vector3d f = calculateF();
        Vector3d b = calculateB();
        Vector3d a = calculateA();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(e), toVertex(f)),
                toLine(UNKNOWN_COLOR_ID, toVertex(f), toVertex(b)),
                toLine(UNKNOWN_COLOR_ID, toVertex(b), toVertex(a)),
                toLine(UNKNOWN_COLOR_ID, toVertex(a), toVertex(e))
        );
    }

    private List<Line> getBackFace() {
        Vector3d g = calculateG();
        Vector3d h = calculateH();
        Vector3d d = calculateD();
        Vector3d c = calculateC();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(g), toVertex(h)),
                toLine(UNKNOWN_COLOR_ID, toVertex(h), toVertex(d)),
                toLine(UNKNOWN_COLOR_ID, toVertex(d), toVertex(c)),
                toLine(UNKNOWN_COLOR_ID, toVertex(c), toVertex(g))
        );
    }

    private List<Line> getLeftFace() {
        Vector3d h = calculateH();
        Vector3d e = calculateE();
        Vector3d a = calculateA();
        Vector3d d = calculateD();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(h), toVertex(e)),
                toLine(UNKNOWN_COLOR_ID, toVertex(e), toVertex(a)),
                toLine(UNKNOWN_COLOR_ID, toVertex(a), toVertex(d)),
                toLine(UNKNOWN_COLOR_ID, toVertex(d), toVertex(h))
        );
    }

    private List<Line> getRightFace() {
        Vector3d f = calculateF();
        Vector3d g = calculateG();
        Vector3d c = calculateC();
        Vector3d b = calculateB();
        return List.of(
                toLine(UNKNOWN_COLOR_ID, toVertex(f), toVertex(g)),
                toLine(UNKNOWN_COLOR_ID, toVertex(g), toVertex(c)),
                toLine(UNKNOWN_COLOR_ID, toVertex(c), toVertex(b)),
                toLine(UNKNOWN_COLOR_ID, toVertex(b), toVertex(f))
        );
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
