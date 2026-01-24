package less.lgeo.tracer;

import less.lgeo.primitive.Model;
import less.lgeo.primitive.Point;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.test.ModelTestUtils;
import less.lgeo.tracer.bvh.BoundingBox;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundBoxTest {


    @Test
    void getBoundingBoxSinglePoint() {
        BoundingBox boundingBox = new BoundingBox();
        Point vertex = new Point(1);

        assertEquals(boundingBox.getMin(), new Point(Double.POSITIVE_INFINITY));
        assertEquals(boundingBox.getMax(), new Point(Double.NEGATIVE_INFINITY));

        boundingBox.growToInclude(vertex);

        assertEquals(vertex, boundingBox.getMin());
        assertEquals(vertex, boundingBox.getCenter());
        assertEquals(vertex, boundingBox.getMax());
    }

    @Test
    void getBoundingBoxThreePoints() {

        Point min = new Point(0, 0, 0);
        Point mid = new Point(0.5, -0.5, 0.5);
        Point max = new Point(1, -1, 1);
        BoundingBox boundingBox = new BoundingBox(List.of(min, mid, max));
        assertTrue(boundingBox.includesPoint(min));
        assertTrue(boundingBox.includesPoint(mid));
        assertTrue(boundingBox.includesPoint(max));
        assertEquals(new Point(0, -1, 0), boundingBox.getMin());
        assertEquals(mid, boundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), boundingBox.getMax());
    }

    @Test
    void getBoundBoxQuads() {
        Model cube = ModelTestUtils.cube();
        List<Quadrilateral> quadrilaterals = cube.quadrilaterals();
        List<Point> vertices = quadrilaterals.stream()
                .flatMap(quadrilateral -> quadrilateral.getVertices().stream()).toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        vertices.forEach(vertex -> assertTrue(boundingBox.includesPoint(vertex)));
    }
}
