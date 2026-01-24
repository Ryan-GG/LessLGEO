package less.lgeo.tracer;

import less.lgeo.primitive.Model;
import less.lgeo.primitive.Point;
import less.lgeo.primitive.Triangle;
import less.lgeo.test.ModelTestUtils;
import less.lgeo.tracer.bvh.BoundingBox;
import less.lgeo.tracer.bvh.BoundingVolumeHierarchy;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import java.util.List;

import static less.lgeo.test.ModelTestUtils.BLACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BoundingVolumeHierarchyTest {

    /**
     * For a cube, each quadrilateral will be split into two triangles and become
     * both each a child from the parent node.
     * Since each triangle extends to the min and max of the polygon, all values
     * should be the same for the bounding box.
     * (i.e. - cube made oof triangles )
     */
    @Test
    void equalSharedPoints() {

        Model cube = ModelTestUtils.cube();
        List<Triangle> triangles = cube.tessellate();

        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

        BoundingVolumeHierarchy.Node root = bvh.getRoot();
        assertNotNull(root, "Root node should not be null");
        assertNotNull(root.getBoundingBox(), "Root bounding box should not be null");

        BoundingBox rootBoundingBox = root.getBoundingBox();
        assertEquals(new Point(0, -1, 0), rootBoundingBox.getMin());
        assertEquals(new Point(0.5, -0.5, 0.5), rootBoundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), rootBoundingBox.getMax());
        assertEquals(new Vector3d(1, 1, 1), rootBoundingBox.getSize());

        BoundingBox childABoundingBox = root.getChildA().getBoundingBox();
        assertEquals(new Point(0, -1, 0), childABoundingBox.getMin());
        assertEquals(new Point(0.5, -0.5, 0.5), childABoundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), childABoundingBox.getMax());
        assertEquals(new Vector3d(1, 1, 1), childABoundingBox.getSize());

        BoundingBox childBBoundingBox = root.getChildB().getBoundingBox();
        assertEquals(new Point(0, -1, 0), childBBoundingBox.getMin());
        assertEquals(new Point(0.5, -0.5, 0.5), childBBoundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), childBBoundingBox.getMax());
        assertEquals(new Vector3d(1, 1, 1), childBBoundingBox.getSize());
    }

    @Test
    void splitOnXAxis() {
        Triangle negative = new Triangle(BLACK,
                new Point(0, 0, 0),
                new Point(0, 0, 1),
                new Point(-1, 0, 0));

        Triangle positive = new Triangle(BLACK,
                new Point(0, 0, 0),
                new Point(0, 0, 1),
                new Point(1, 0, 0));

        List<Triangle> triangles = List.of(negative, positive);

        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

        BoundingVolumeHierarchy.Node root = bvh.getRoot();
        BoundingBox rootBoundingBox = root.getBoundingBox();
        assertEquals(new Point(-1, 0, 0), rootBoundingBox.getMin());
        assertEquals(new Point(0, 0, 0.5), rootBoundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), rootBoundingBox.getMax());

        BoundingBox childABox = root.getChildA().getBoundingBox();
        assertEquals(new Point(-1, 0, 0), childABox.getMin());
        assertEquals(new Point(-0.5, 0, 0.5), childABox.getCenter());
        assertEquals(new Point(0, 0, 1), childABox.getMax());

        BoundingBox childBBox = root.getChildB().getBoundingBox();
        assertEquals(new Point(0, 0, 0), childBBox.getMin());
        assertEquals(new Point(0.5, 0, 0.5), childBBox.getCenter());
        assertEquals(new Point(1, 0, 1), childBBox.getMax());
    }

    @Test
    void splitOnYAxis() {
        Triangle negative = new Triangle(BLACK,
                new Point(0, 0, 0),
                new Point(0, -1, 0),
                new Point(1, 0, 0));

        Triangle positive = new Triangle(BLACK,
                new Point(0, 0, 0),
                new Point(0, 1, 0),
                new Point(1, 0, 0));

        List<Triangle> triangles = List.of(negative, positive);

        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

        BoundingVolumeHierarchy.Node root = bvh.getRoot();
        BoundingBox rootBoundingBox = root.getBoundingBox();
        assertEquals(new Point(0, -1, 0), rootBoundingBox.getMin());
        assertEquals(new Point(0.5, 0, 0), rootBoundingBox.getCenter());
        assertEquals(new Point(1, 1, 0), rootBoundingBox.getMax());

        BoundingBox childABox = root.getChildA().getBoundingBox();
        assertEquals(new Point(0, -1, 0), childABox.getMin());
        assertEquals(new Point(0.5, -0.5, 0), childABox.getCenter());
        assertEquals(new Point(1, 0, 0), childABox.getMax());

        BoundingBox childBBox = root.getChildB().getBoundingBox();
        assertEquals(new Point(0, 0, 0), childBBox.getMin());
        assertEquals(new Point(0.5, 0.5, 0), childBBox.getCenter());
        assertEquals(new Point(1, 1, 0), childBBox.getMax());
    }

    @Test
    void splitOnZAxis() {
        Triangle negative = new Triangle(BLACK,
                new Point(0, 0, -1),
                new Point(0, 0, 0),
                new Point(1, 0, 0));

        Triangle positive = new Triangle(BLACK,
                new Point(0, 0, 1),
                new Point(0, 0, 0),
                new Point(1, 0, 0));

        List<Triangle> triangles = List.of(negative, positive);

        BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);

        BoundingVolumeHierarchy.Node root = bvh.getRoot();
        BoundingBox rootBoundingBox = root.getBoundingBox();
        assertEquals(new Point(0, 0, -1), rootBoundingBox.getMin());
        assertEquals(new Point(0.5, 0, 0), rootBoundingBox.getCenter());
        assertEquals(new Point(1, 0, 1), rootBoundingBox.getMax());

        BoundingBox childABox = root.getChildA().getBoundingBox();
        assertEquals(new Point(0, 0, -1), childABox.getMin());
        assertEquals(new Point(0.5, 0, -0.5), childABox.getCenter());
        assertEquals(new Point(1, 0, 0), childABox.getMax());

        BoundingBox childBBox = root.getChildB().getBoundingBox();
        assertEquals(new Point(0, 0, 0), childBBox.getMin());
        assertEquals(new Point(0.5, 0, 0.5), childBBox.getCenter());
        assertEquals(new Point(1, 0, 1), childBBox.getMax());
    }
}
