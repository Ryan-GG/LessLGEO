package less.lgeo.tracer;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.test.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static less.lgeo.common.VertexUtils.toVertex;
import static less.lgeo.primitive.QuadrilateralUtils.getVertices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundBoxTest {


    @Test
    void getBoundingBoxSinglePoint() {
        BoundingBox boundingBox = new BoundingBox();
        Vertex vertex = toVertex(1, 1, 1);

        assertEquals(boundingBox.getMin(), new Vector3d(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(boundingBox.getMax(), new Vector3d(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));

        boundingBox.growToInclude(vertex);

        assertEquals(boundingBox.getMin(), new Vector3d(1));
        assertEquals(boundingBox.getMax(), new Vector3d(1));
    }

    @Test
    void getBoundingBoxThreePoints() {

        Vertex min = toVertex(0, 0, 0);
        Vertex mid = toVertex(0.5, -0.5, -0.5);
        Vertex max = toVertex(1, -1, 1);
        BoundingBox boundingBox = new BoundingBox(List.of(min, mid, max));
        assertTrue(boundingBox.includesPoint(min));
        assertTrue(boundingBox.includesPoint(mid));
        assertTrue(boundingBox.includesPoint(max));
        assertEquals(boundingBox.getMin(), new Vector3d(0, 0, -0.5));
        assertEquals(boundingBox.getMax(), new Vector3d(1, -1, 1));
    }

    @Test
    void getBoundBoxQuads() {
        Model cube = ModelTestUtils.cube().build();
        Set<Quadrilateral> quadrilaterals = ModelUtils.getQuadrilaterals(cube);
        List<Vertex> vertices = quadrilaterals.stream().flatMap(quadrilateral -> getVertices(quadrilateral).stream()).toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        vertices.forEach(vertex -> assertTrue(boundingBox.includesPoint(vertex)));
    }
}
