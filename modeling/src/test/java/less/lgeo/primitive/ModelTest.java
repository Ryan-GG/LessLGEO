package less.lgeo.primitive;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static less.lgeo.util.ModelTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelTest {

    @Nested
    class Transform {
        Model nestedCubes = nestedCubes();
        Model transformed = nestedCubes.transformModel();

        @Test
        void testColorInheritance() {
            assertTrue(transformed.lines().isEmpty());
            assertTrue(transformed.triangles().isEmpty());
            assertTrue(transformed.quadrilaterals().isEmpty());
            assertTrue(transformed.optionalLines().isEmpty());
            assertEquals(BLACK, transformed.pieces().getFirst().color());
            assertEquals(RED, transformed.pieces().getLast().color());
        }
    }
}
