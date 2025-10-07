package less.lgeo.tracer;

import static less.lgeo.common.VertexUtils.toVector3d;

import java.util.List;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;


/**
 * Represents a 3D axis-aligned box defined by its 8 vertices.
 * <p>
 * Coordinate system:
 * <ul>
 *   <li>+X → right</li>
 *   <li>−Y → up</li>
 *   <li>+Z → forward (toward H)</li>
 * </ul>
 *
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
 *
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
 *
 * Faces:
 * <ul>
 *   <li>Top (up): A–B–C–D (−Y)</li>
 *   <li>Bottom (down): E–F–G–H (+Y)</li>
 *   <li>Front (facing +Z): D–C–G–H</li>
 *   <li>Back (facing −Z): A–B–F–E</li>
 *   <li>Left (−X): A–D–H–E</li>
 *   <li>Right (+X): B–C–G–F</li>
 * </ul>
 *
 * This vertex labeling follows a conventional box layout where:
 * <ul>
 *   <li>–Y is up</li>
 *   <li>+Z extends forward</li>
 *   <li>+X extends to the right</li>
 * </ul>
 *
 * Useful for visualization, collision detection, and geometric computations.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBox {

  private Vector3d min = new Vector3d(Double.POSITIVE_INFINITY);
  private Vector3d max = new Vector3d(Double.NEGATIVE_INFINITY);
  private Vector3d center = calculateCenter();
  private Vector3d a = calculateA();
  private Vector3d b  = calculateA();
  private Vector3d c = calculateA();
  private Vector3d d = calculateA();
  private Vector3d e = calculateA();
  private Vector3d f = calculateA();
  private Vector3d g = calculateA();
  private Vector3d h = calculateA();

  public BoundingBox(List<Vertex> vertices) {
    vertices.forEach(this::growToInclude);
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

  public List<Quadrilateral> getBoundingBoxAsQuadrilaterals()
  {
    Quadrilateral left = Quadrilateral.newBuilder()
        .setP1()
        .setP2()
        .setP3()
        .setP4()
        .setColorId(0) // FIXME, just make white / pink
        .build();
    Quadrilateral right = ;
    Quadrilateral top = ;
    Quadrilateral bottom =;
    Quadrilateral front = ;
    Quadrilateral back  = ;
  }

}
