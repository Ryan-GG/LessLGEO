package less.lgeo.test;

import less.lgeo.common.Vector3;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Model.Builder;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;

import java.util.List;

import static less.lgeo.common.Vector3Utils.toVector3;
import static less.lgeo.primitive.QuadrilateralUtils.toQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.toTriangle;

/**
 * Test Utility file for creating {@link Model}
 */
public class ModelTestUtils {

    public static int UNKNOWN_COLOR_ID = -1;

    public static Builder newModelEmpty() {
        return Model.newBuilder();
    }

    /**
     * See BoundingBox for ordering
     *
     * @return {@link Model} shaped as a Cube
     */
    public static Model.Builder cube() {

        Vector3 a = toVector3(0, -1, 0);
        Vector3 b = toVector3(1, -1, 0);
        Vector3 c = toVector3(1, -1, 1);
        Vector3 d = toVector3(0, -1, 1);

        Vector3 e = toVector3(0, 0, 0);
        Vector3 f = toVector3(1, 0, 0);
        Vector3 g = toVector3(1, 0, 1);
        Vector3 h = toVector3(0, 0, 1);

        Quadrilateral top = toQuadrilateral(UNKNOWN_COLOR_ID, a, b, c, d);
        Quadrilateral bottom = toQuadrilateral(UNKNOWN_COLOR_ID, e, f, g, h);

        Quadrilateral front = toQuadrilateral(UNKNOWN_COLOR_ID, e, f, b, a);
        Quadrilateral back = toQuadrilateral(UNKNOWN_COLOR_ID, g, h, d, c);

        Quadrilateral left = toQuadrilateral(UNKNOWN_COLOR_ID, h, e, a, d);
        Quadrilateral right = toQuadrilateral(UNKNOWN_COLOR_ID, f, g, c, b);

        List<Quadrilateral> cube = List.of(top, bottom, front, back, left, right);

        return newModelEmpty()
                .addAllQuadrilateral(cube);
    }

    public static Model.Builder pyramid() {

        Vector3 a = toVector3(0, 0, 0);
        Vector3 b = toVector3(0, 0, 1);
        Vector3 c = toVector3(1, 0, 1);
        Vector3 d = toVector3(1, 0, 0);
        Vector3 e = toVector3(0.5, -1, 0.5);

        Quadrilateral bottom = toQuadrilateral(UNKNOWN_COLOR_ID, a, b, c, d);
        Triangle front = toTriangle(UNKNOWN_COLOR_ID, a, e, d);
        Triangle back = toTriangle(UNKNOWN_COLOR_ID, b, e, c);
        Triangle left = toTriangle(UNKNOWN_COLOR_ID, a, e, b);
        Triangle right = toTriangle(UNKNOWN_COLOR_ID, d, e, c);

        List<Triangle> pyramid = List.of(front, back, left, right);

        return newModelEmpty()
                .addAllTriangle(pyramid)
                .addQuadrilateral(bottom);
    }

}
