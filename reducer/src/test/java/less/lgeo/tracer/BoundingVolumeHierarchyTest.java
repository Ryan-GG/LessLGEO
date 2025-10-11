package less.lgeo.tracer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.ModelUtils;
import less.lgeo.primitive.Triangle;
import less.lgeo.test.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

public class BoundingVolumeHierarchyTest {

  @Test
  void testBVHConstructionAndBoundingBoxes() {

    Model cube = ModelTestUtils.cube().build();
    //Tessellation is fine
    List<Triangle> triangles = ModelUtils.tessellateModel(cube);

    // Create BVH
    BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

    // Root should not be null
    assertNotNull(bvh.getRoot(), "Root node should not be null");

    // Root bounding box should not be null
    assertNotNull(bvh.getRoot().getBoundingBox(), "Root bounding box should not be null");

    BoundingBox rootBoundingBox = bvh.getRoot().getBoundingBox();
    assertEquals(new Vector3d(0, 0, 0), rootBoundingBox.getMin());
    assertEquals(new Vector3d(0.5, -0.5, 0.5), rootBoundingBox.getCenter());
    assertEquals(new Vector3d(1, -1, 1), rootBoundingBox.getMin());
  }
}
