package less.lgeo.tracer.bvh;

import less.lgeo.primitive.Triangle;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoundingVolumeHierarchy {

    //FIXME, put this into application.yaml
    private static final int MAX_DEPTH = 20;
    private final Node root;

    public BoundingVolumeHierarchy(List<Triangle> triangles) {
        List<Vector3d> vertices = triangles.stream()
                .flatMap(triangle -> triangle.getVertices().stream())
                .toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        this.root = new Node(boundingBox, triangles);
        this.root.split();
    }

    @Getter
    @NoArgsConstructor
    public static class Node {

        private BoundingBox boundingBox;
        private List<Triangle> triangles = new ArrayList<>();
        private Node childA;
        private Node childB;

        public Node(BoundingBox boundingBox, List<Triangle> triangles) {
            this.boundingBox = boundingBox;
            this.triangles = new ArrayList<>(triangles);
        }

        public void split() {
            split(0);
        }

        /**
         * @return index into a {@link org.joml.Vector3d} based on longest axis of a {@link BoundingBox}
         * 0(x), 1(y), 2(z)
         */
        private int getSplitComponentIndex() {
            Vector3d size = boundingBox.getSize();
            return size.x() > Math.max(size.y(), size.z()) ? 0 : size.y() > size.z() ? 1 : 2;
        }

        private void split(int currentDepth) {
            if (MAX_DEPTH <= currentDepth || triangles.size() <= 1) {
                return;
            }

            int splitAxis = getSplitComponentIndex();
            List<Triangle> aList = new ArrayList<>();
            List<Triangle> bList = new ArrayList<>();

            for (Triangle triangle : triangles) {
                boolean inA = triangle.getCentroid().get(splitAxis) < boundingBox.getCenter().get(splitAxis);
                (inA ? aList : bList).add(triangle);
            }

            // No effective split, stop to prevent pointless split
            if (aList.isEmpty() || bList.isEmpty()) {
                return;
            }

            BoundingBox leftBox = new BoundingBox(aList.stream()
                    .flatMap(triangle -> triangle.getVertices().stream()).toList());
            BoundingBox rightBox = new BoundingBox(bList.stream()
                    .flatMap(triangle -> triangle.getVertices().stream()).toList());

            childA = new Node(leftBox, aList);
            childB = new Node(rightBox, bList);

            childA.split(currentDepth + 1);
            childB.split(currentDepth + 1);
        }

    }
}
