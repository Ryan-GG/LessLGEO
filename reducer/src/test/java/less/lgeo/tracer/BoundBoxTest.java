package less.lgeo.tracer;

import less.lgeo.primitive.Model;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.test.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundBoxTest {


    @Test
    void getBoundingBoxSinglePoint() {
        BoundingBox boundingBox = new BoundingBox();
        Vector3d vertex = new Vector3d(1);

        assertEquals(boundingBox.getMin(), new Vector3d(Double.POSITIVE_INFINITY));
        assertEquals(boundingBox.getMax(), new Vector3d(Double.NEGATIVE_INFINITY));

        boundingBox.growToInclude(vertex);

        assertEquals(vertex, boundingBox.getMin());
        assertEquals(vertex, boundingBox.getCenter());
        assertEquals(vertex, boundingBox.getMax());
    }

    @Test
    void getBoundingBoxThreePoints() {

        Vector3d min = new Vector3d(0, 0, 0);
        Vector3d mid = new Vector3d(0.5, -0.5, 0.5);
        Vector3d max = new Vector3d(1, -1, 1);
        BoundingBox boundingBox = new BoundingBox(List.of(min, mid, max));
        assertTrue(boundingBox.includesPoint(min));
        assertTrue(boundingBox.includesPoint(mid));
        assertTrue(boundingBox.includesPoint(max));
        assertEquals(new Vector3d(0, -1, 0), boundingBox.getMin());
        assertEquals(mid, boundingBox.getCenter());
        assertEquals(new Vector3d(1, 0, 1), boundingBox.getMax());
    }

    @Test
    void getBoundBoxQuads() {
        Model cube = ModelTestUtils.cube();
        List<Quadrilateral> quadrilaterals = cube.quadrilaterals();
        List<Vector3d> vertices = quadrilaterals.stream()
                .flatMap(quadrilateral -> quadrilateral.getVertices().stream()).toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        vertices.forEach(vertex -> assertTrue(boundingBox.includesPoint(vertex)));
    }
}
