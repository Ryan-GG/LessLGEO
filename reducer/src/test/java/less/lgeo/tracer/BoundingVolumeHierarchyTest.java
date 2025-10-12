package less.lgeo.tracer;

import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.Triangle;
import less.lgeo.test.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BoundingVolumeHierarchyTest {

    /**
     * For a cube, each quadrilateral will be split into two triangles and become both each a child from the parent node.
     * Since each triangle extends to the min and max of the polygon, all values should be the same for the bounding box.
     * (i.e. - cube made oof triangles )
     */
    @Test
    void testBVHWithSharedPoints() {

        Model cube = ModelTestUtils.cube().build();
        List<Triangle> triangles = ModelUtils.tessellateModel(cube);

        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

        BoundingVolumeHierarchy.Node root = bvh.getRoot();
        assertNotNull(root, "Root node should not be null");
        assertNotNull(root.getBoundingBox(), "Root bounding box should not be null");

        BoundingBox rootBoundingBox = root.getBoundingBox();
        assertEquals(new Vector3d(0, -1, 0), rootBoundingBox.getMin());
        assertEquals(new Vector3d(0.5, -0.5, 0.5), rootBoundingBox.getCenter());
        assertEquals(new Vector3d(1, 0, 1), rootBoundingBox.getMax());

        BoundingBox childABoundingBox = root.getChildA().getBoundingBox();
        assertEquals(new Vector3d(0, -1, 0), childABoundingBox.getMin());
        assertEquals(new Vector3d(0.5, -0.5, 0.5), childABoundingBox.getCenter());
        assertEquals(new Vector3d(1, 0, 1), childABoundingBox.getMax());

        BoundingBox childBBoundingBox = root.getChildB().getBoundingBox();
        assertEquals(new Vector3d(0, -1, 0), childBBoundingBox.getMin());
        assertEquals(new Vector3d(0.5, -0.5, 0.5), childBBoundingBox.getCenter());
        assertEquals(new Vector3d(1, 0, 1), childBBoundingBox.getMax());
    }
}
