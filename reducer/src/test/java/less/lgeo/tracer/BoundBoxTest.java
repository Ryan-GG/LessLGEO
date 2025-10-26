package less.lgeo.tracer;

import less.lgeo.common.Vector3;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.test.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundBoxTest {


    @Test
    void getBoundingBoxSinglePoint() {
        BoundingBox boundingBox = new BoundingBox();
        Vector3 vertex = new Vector3(1, 1, 1);

        assertEquals(boundingBox.getMin(), new Vector3d(Double.POSITIVE_INFINITY));
        assertEquals(boundingBox.getMax(), new Vector3d(Double.NEGATIVE_INFINITY));

        boundingBox.growToInclude(vertex);

        assertEquals(new Vector3d(1), boundingBox.getMin());
        assertEquals(new Vector3d(1), boundingBox.getCenter());
        assertEquals(new Vector3d(1), boundingBox.getMax());
    }

    @Test
    void getBoundingBoxThreePoints() {

        Vector3 min = new Vector3(0, 0, 0);
        Vector3 mid = new Vector3(0.5, -0.5, 0.5);
        Vector3 max = new Vector3(1, -1, 1);
        BoundingBox boundingBox = new BoundingBox(List.of(min, mid, max));
        assertTrue(boundingBox.includesPoint(min));
        assertTrue(boundingBox.includesPoint(mid));
        assertTrue(boundingBox.includesPoint(max));
        assertEquals(new Vector3d(0, -1, 0), boundingBox.getMin());
        assertEquals(new Vector3d(mid.x(), mid.y(), mid.z()), boundingBox.getCenter());
        assertEquals(new Vector3d(1, 0, 1), boundingBox.getMax());
    }

    @Test
    void getBoundBoxQuads() {
        Model cube = ModelTestUtils.cube();
        Set<Quadrilateral> quadrilaterals = cube.getQuadrilaterals();
        List<Vector3> vertices = quadrilaterals.stream()
                .flatMap(quadrilateral -> quadrilateral.getVertices().stream()).toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        vertices.forEach(vertex -> assertTrue(boundingBox.includesPoint(vertex)));
    }
}
