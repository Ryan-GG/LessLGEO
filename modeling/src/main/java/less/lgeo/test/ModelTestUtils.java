package less.lgeo.test;

import less.lgeo.primitive.Model;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import org.joml.Vector3d;

import java.util.List;


/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {

    public static int UNKNOWN_COLOR_ID = -1;

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

        Quadrilateral top = new Quadrilateral(UNKNOWN_COLOR_ID, a, b, c, d);
        Quadrilateral bottom = new Quadrilateral(UNKNOWN_COLOR_ID, e, f, g, h);

        Quadrilateral front = new Quadrilateral(UNKNOWN_COLOR_ID, e, f, b, a);
        Quadrilateral back = new Quadrilateral(UNKNOWN_COLOR_ID, g, h, d, c);

        Quadrilateral left = new Quadrilateral(UNKNOWN_COLOR_ID, h, e, a, d);
        Quadrilateral right = new Quadrilateral(UNKNOWN_COLOR_ID, f, g, c, b);

        List<Quadrilateral> cube = List.of(top, bottom, front, back, left, right);

        return Model.builder()
                .quadrilaterals(cube)
                .build();
    }

    public static Model pyramid() {

        Vector3d a = new Vector3d(0, 0, 0);
        Vector3d b = new Vector3d(0, 0, 1);
        Vector3d c = new Vector3d(1, 0, 1);
        Vector3d d = new Vector3d(1, 0, 0);
        Vector3d e = new Vector3d(0.5, -1, 0.5);

        Quadrilateral bottom = new Quadrilateral(UNKNOWN_COLOR_ID, a, b, c, d);
        Triangle front = new Triangle(UNKNOWN_COLOR_ID, a, e, d);
        Triangle back = new Triangle(UNKNOWN_COLOR_ID, b, e, c);
        Triangle left = new Triangle(UNKNOWN_COLOR_ID, a, e, b);
        Triangle right = new Triangle(UNKNOWN_COLOR_ID, d, e, c);

        List<Triangle> pyramid = List.of(front, back, left, right);

        return Model.builder()
                .triangles(pyramid)
                .quadrilaterals(List.of(bottom))
                .build();
    }

}
