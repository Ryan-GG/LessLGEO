package less.lgeo.tracer;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.test.ModelTestUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static less.lgeo.primitive.QuadrilateralUtils.getVertices;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundBoxTest {

    @Test
    void getBoundBoxQuads() {
        Model cube = ModelTestUtils.cube().build();
        Set<Quadrilateral> quadrilaterals = ModelUtils.getQuadrilaterals(cube);
        List<Vertex> vertices = quadrilaterals.stream().flatMap(quadrilateral -> getVertices(quadrilateral).stream()).toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        List<Quadrilateral> boundingBoxQuads = boundingBox.getBoundingBoxAsQuadrilaterals();

        quadrilaterals.forEach(quadrilateral -> assertTrue(boundingBoxQuads.contains(quadrilateral), () ->
                String.format("Bounding Box Quads\n%s does not contain:\n%s", boundingBox, quadrilateral.toString())));
    }
}
