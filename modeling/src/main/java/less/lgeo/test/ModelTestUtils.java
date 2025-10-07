package less.lgeo.test;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Model.Builder;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;

import java.util.List;

import static less.lgeo.common.VertexUtils.toVertex;
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
     * See BoundingBox
     *
     * @return
     */
    public static Model.Builder cube() {

        Vertex a = toVertex(0, -1, 0);
        Vertex b = toVertex(1, -1, 0);
        Vertex c = toVertex(1, -1, 1);
        Vertex d = toVertex(0, -1, 1);

        Vertex e = toVertex(0, 0, 0);
        Vertex f = toVertex(1, 0, 0);
        Vertex g = toVertex(1, 0, 1);
        Vertex h = toVertex(0, 0, 1);

        Quadrilateral top = toQuadrilateral(UNKNOWN_COLOR_ID, a, b, c, d);
        Quadrilateral bottom = toQuadrilateral(UNKNOWN_COLOR_ID, e, f, g, h);

        Quadrilateral front = toQuadrilateral(UNKNOWN_COLOR_ID, e, f, b, a);
        Quadrilateral back = toQuadrilateral(UNKNOWN_COLOR_ID, g, h, d, c);
        
        Quadrilateral left = toQuadrilateral(UNKNOWN_COLOR_ID, h, e, a, d);
        Quadrilateral right = toQuadrilateral(UNKNOWN_COLOR_ID, f, g, b, c);

        List<Quadrilateral> cube = List.of(top, bottom, front, back, left, right);

        return newModelEmpty()
                .addAllQuadrilateral(cube);
    }

    public static Model.Builder pyramid() {

        Vertex a = toVertex(0, 0, 0);
        Vertex b = toVertex(0, 0, 1);
        Vertex c = toVertex(1, 0, 1);
        Vertex d = toVertex(1, 0, 0);
        Vertex e = toVertex(0.5, -1, 0.5);

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
