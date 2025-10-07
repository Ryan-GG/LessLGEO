package less.lgeo.tracer;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import org.joml.Vector3d;

import java.util.List;

import static less.lgeo.common.VertexUtils.toVector3d;
import static less.lgeo.common.VertexUtils.toVertex;
import static less.lgeo.primitive.QuadrilateralUtils.toQuadrilateral;
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
 * <p>
 * Vertex mapping:
 * <ul>
 *   <li>A = (minX, minY, minZ)</li>
 *   <li>B = (maxX, minY, minZ)</li>
 *   <li>C = (maxX, minY, maxZ)</li>
 *   <li>D = (minX, minY, maxZ)</li>
 *   <li>E = (minX, maxY, minZ)</li>
 *   <li>F = (maxX, maxY, minZ)</li>
 *   <li>G = (maxX, maxY, maxZ)</li>
 *   <li>H = (minX, maxY, maxZ)</li>
 * </ul>
 * <p>
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
public class BoundingBox {

    private Vector3d min = new Vector3d(Double.POSITIVE_INFINITY);
    private final Vector3d a = calculateA();
    private Vector3d max = new Vector3d(Double.NEGATIVE_INFINITY);
    private final Vector3d b = calculateB();
    private final Vector3d c = calculateC();
    private final Vector3d d = calculateD();
    private final Vector3d e = calculateE();
    private final Vector3d f = calculateF();
    private final Vector3d g = calculateG();
    private final Vector3d h = calculateH();

    public BoundingBox(List<Vertex> vertices) {
        vertices.forEach(this::growToInclude);
    }

    private Vector3d calculateA() {
        return min;
    }

    private Vector3d calculateB() {
        return new Vector3d(max.x, min.y, min.z);
    }

    private Vector3d calculateC() {
        return new Vector3d(max.x, min.y, max.z);
    }

    private Vector3d calculateD() {
        return new Vector3d(min.x, min.y, max.z);
    }

    private Vector3d calculateE() {
        return new Vector3d(min.x, max.y, min.z);
    }

    private Vector3d calculateF() {
        return new Vector3d(max.x, max.y, min.z);
    }

    private Vector3d calculateG() {
        return max;
    }

    private Vector3d calculateH() {
        return new Vector3d(min.x, max.y, max.z);
    }

    //TODO, these may be incorrect since -Y is actually larger than positive Y
    public void growToInclude(Vertex point) {
        growToInclude(toVector3d(point));
    }

    public void growToInclude(Vector3d point) {
        min = min.min(point);
        max = max.max(point);
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
        return (min.add(max)).mul(0.5);
    }

    public List<Quadrilateral> getBoundingBoxAsQuadrilaterals() {
        return List.of(
                getTopFace(),
                getBottomFace(),
                getFrontFace(),
                getBackFace(),
                getLeftFace(),
                getRightFace()
        );
    }

    private Quadrilateral getTopFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(a), toVertex(b), toVertex(c), toVertex(d));
    }

    private Quadrilateral getBottomFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(e), toVertex(f), toVertex(g), toVertex(h));
    }

    private Quadrilateral getFrontFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(e), toVertex(f), toVertex(b), toVertex(a));
    }

    private Quadrilateral getBackFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(g), toVertex(h), toVertex(d), toVertex(c));
    }

    private Quadrilateral getLeftFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(h), toVertex(e), toVertex(a), toVertex(d));
    }

    private Quadrilateral getRightFace() {
        return toQuadrilateral(UNKNOWN_COLOR_ID, toVertex(f), toVertex(g), toVertex(c), toVertex(b));
    }

    @Override
    public String toString() {
        return String.format(
                """
                        Top: %s,
                        Bottom: %s,
                        Front: %s,
                        Back: %s,
                        Left: %s,
                        Right: %s
                        """,
                getTopFace(),
                getBottomFace(),
                getFrontFace(),
                getBackFace(),
                getLeftFace(),
                getRightFace()
        );
    }


}
