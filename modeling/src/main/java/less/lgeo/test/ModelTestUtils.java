package less.lgeo.test;

import less.lgeo.common.Color;
import less.lgeo.common.Matrix;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;
import org.joml.Vector3d;

import java.util.List;
import java.util.Optional;


/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {

    public static final Color BLACK = new Color(0, "Black", "#000000", false);
    public static final Color RED = new Color(4, "Red", "#FF0000", false);

    public static final Color INHERIT_PARENT_COLOR = new Color(16, "inherit_parent", "", false);
    public static final Color INHERIT_EDGE_COLOR = new Color(24, "inherit_edge", "", false);


    /**
     * See BoundingBox for ordering
     *
     * @return {@link Model} shaped as a Cube
     */
    public static Model cube() {

        Vector3d a = new Vector3d(0, -1, 0);
        Vector3d b = new Vector3d(1, -1, 0);
        Vector3d c = new Vector3d(1, -1, 1);
        Vector3d d = new Vector3d(0, -1, 1);

        Vector3d e = new Vector3d(0, 0, 0);
        Vector3d f = new Vector3d(1, 0, 0);
        Vector3d g = new Vector3d(1, 0, 1);
        Vector3d h = new Vector3d(0, 0, 1);

        Quadrilateral top = new Quadrilateral(INHERIT_PARENT_COLOR, a, b, c, d);
        Quadrilateral bottom = new Quadrilateral(INHERIT_PARENT_COLOR, e, f, g, h);

        Quadrilateral front = new Quadrilateral(INHERIT_PARENT_COLOR, e, f, b, a);
        Quadrilateral back = new Quadrilateral(INHERIT_PARENT_COLOR, g, h, d, c);

        Quadrilateral left = new Quadrilateral(INHERIT_PARENT_COLOR, h, e, a, d);
        Quadrilateral right = new Quadrilateral(INHERIT_PARENT_COLOR, f, g, c, b);

        List<Quadrilateral> cube = List.of(top, bottom, front, back, left, right);

        return new Model(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                cube,
                List.of(),
                List.of());
    }

    public static Model pyramid() {

        Vector3d a = new Vector3d(0, 0, 0);
        Vector3d b = new Vector3d(0, 0, 1);
        Vector3d c = new Vector3d(1, 0, 1);
        Vector3d d = new Vector3d(1, 0, 0);
        Vector3d e = new Vector3d(0.5, -1, 0.5);

        Quadrilateral bottom = new Quadrilateral(BLACK, a, b, c, d);
        Triangle front = new Triangle(BLACK, a, e, d);
        Triangle back = new Triangle(BLACK, b, e, c);
        Triangle left = new Triangle(BLACK, a, e, b);
        Triangle right = new Triangle(BLACK, d, e, c);

        List<Triangle> pyramid = List.of(front, back, left, right);

        return new Model(
                List.of(),
                List.of(),
                List.of(),
                pyramid,
                List.of(bottom),
                List.of(),
                List.of()
        );
    }

    public static Model nestedCubes() {
        return new Model(
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(
                        new SubFileReference(BLACK, Matrix.IDENTITY_MATRIX, cube(), "cube1", Optional.empty()),
                        new SubFileReference(RED, Matrix.IDENTITY_MATRIX, cube(), "cube2", Optional.empty())
                )
        );
    }


}
