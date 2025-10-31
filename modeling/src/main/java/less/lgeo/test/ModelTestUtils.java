package less.lgeo.test;

import less.lgeo.common.Color;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import org.joml.Vector3d;

import java.util.List;


/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {
    
    public static Color BLACK = new Color(0, "Black", "#000000", false);

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

        Quadrilateral top = new Quadrilateral(BLACK, a, b, c, d);
        Quadrilateral bottom = new Quadrilateral(BLACK, e, f, g, h);

        Quadrilateral front = new Quadrilateral(BLACK, e, f, b, a);
        Quadrilateral back = new Quadrilateral(BLACK, g, h, d, c);

        Quadrilateral left = new Quadrilateral(BLACK, h, e, a, d);
        Quadrilateral right = new Quadrilateral(BLACK, f, g, c, b);

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

}
