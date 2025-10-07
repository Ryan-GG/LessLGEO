package less.lgeo.tracer;

import less.lgeo.common.LineType;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;

import java.util.List;

import static less.lgeo.common.VertexUtils.toVector3d;
import static less.lgeo.common.VertexUtils.toVertex;
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
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBox {

    private Vector3d min = new Vector3d(Double.POSITIVE_INFINITY);
    private Vector3d max = new Vector3d(Double.NEGATIVE_INFINITY);
    private Vector3d center = calculateCenter();
    private Vector3d a = calculateA();
    private Vector3d b = calculateB();
    private Vector3d c = calculateC();
    private Vector3d d = calculateD();
    private Vector3d e = calculateE();
    private Vector3d f = calculateF();
    private Vector3d g = calculateG();
    private Vector3d h = calculateH();

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
        min = min.min(toVector3d(point));
        max = max.max(toVector3d(point));
    }

    public void growToInclude(Vector3d point) {
        min = min.min(point);
        max = max.max(point);
    }

    public void growToInclude(Triangle triangle) {
        growToInclude(toVector3d(triangle.getP1()));
        growToInclude(toVector3d(triangle.getP2()));
        growToInclude(toVector3d(triangle.getP3()));
    }

    private Vector3d calculateCenter() {
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
        return Quadrilateral.newBuilder()
                .setP1(toVertex(a))
                .setP2(toVertex(b))
                .setP3(toVertex(c))
                .setP4(toVertex(d))
                .setColorId(UNKNOWN_COLOR_ID)
                .build();
    }

    private Quadrilateral getBottomFace() {
        return Quadrilateral.newBuilder()
                .setP1(toVertex(e))
                .setP2(toVertex(f))
                .setP3(toVertex(g))
                .setP4(toVertex(h))
                .setColorId(UNKNOWN_COLOR_ID)
                .setType(LineType.QUADRILATERAL)
                .build();
    }

    private Quadrilateral getFrontFace() {
        return Quadrilateral.newBuilder()
                .setP1(toVertex(e))
                .setP2(toVertex(f))
                .setP3(toVertex(b))
                .setP4(toVertex(a))
                .setColorId(UNKNOWN_COLOR_ID)
                .setType(LineType.QUADRILATERAL)
                .build();
    }
    
    private Quadrilateral getBackFace() {
        return Quadrilateral.newBuilder()
                .setP1(toVertex(g))
                .setP2(toVertex(h))
                .setP3(toVertex(d))
                .setP4(toVertex(c))
                .setColorId(UNKNOWN_COLOR_ID)
                .setType(LineType.QUADRILATERAL)
                .build();
    }

    private Quadrilateral getLeftFace() {
        return Quadrilateral.newBuilder()
                .setP1(toVertex(h))
                .setP2(toVertex(e))
                .setP3(toVertex(a))
                .setP4(toVertex(d))
                .setColorId(UNKNOWN_COLOR_ID)
                .setType(LineType.QUADRILATERAL)
                .build();
    }

    private Quadrilateral getRightFace() {
        return Quadrilateral.newBuilder()
                .setP1(toVertex(f))
                .setP2(toVertex(g))
                .setP3(toVertex(c))
                .setP4(toVertex(b))
                .setColorId(UNKNOWN_COLOR_ID)
                .setType(LineType.QUADRILATERAL)
                .build();
    }


}
