package less.lgeo.tracer;

import less.lgeo.common.Vertex;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Triangle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static less.lgeo.primitive.TriangleUtils.getCentroid;
import static less.lgeo.primitive.TriangleUtils.getVertices;

@Getter
public class BoundingVolumeHierarchy {

    private static final int MAX_DEPTH = 20;
    private final Node root;

    public BoundingVolumeHierarchy(List<Triangle> triangles) {
        List<Vertex> vertices = triangles.stream()
                .flatMap(triangle -> getVertices(triangle).stream())
                .toList();

        BoundingBox boundingBox = new BoundingBox(vertices);
        Node root = new Node(boundingBox, triangles);
        root.split(0);
        this.root = root;
    }

    public List<Line> getBoundingBoxes() {
        return getBoundBoxOfNode(root);
    }

    private List<Line> getBoundBoxOfNode(Node node) {
        if (node == null || node.getBoundingBox() == null) {
            return List.of();
        }

        List<Line> lines = new ArrayList<>(node.getBoundingBox().getBoundingBoxAsLines());
        if (node.getChildA() != null) {
            lines.addAll(getBoundBoxOfNode(node.getChildA()));
        }
        if (node.getChildB() != null) {
            lines.addAll(getBoundBoxOfNode(node.getChildB()));
        }
        return lines;
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

        public void split(int depth) {
            if (depth >= MAX_DEPTH || triangles.size() <= 1) {
                return;
            }

            double midX = boundingBox.getCenter().x();
            List<Triangle> left = new ArrayList<>();
            List<Triangle> right = new ArrayList<>();

            for (Triangle triangle : triangles) {
                boolean inLeft = getCentroid(triangle).getX() < midX;
                (inLeft ? left : right).add(triangle);
            }

            // No effective split, stop to prevent pointless split
            if (left.isEmpty() || right.isEmpty()) {
                return;
            }

            BoundingBox leftBox = new BoundingBox(left.stream()
                    .flatMap(t -> getVertices(t).stream()).toList());
            BoundingBox rightBox = new BoundingBox(right.stream()
                    .flatMap(t -> getVertices(t).stream()).toList());

            childA = new Node(leftBox, left);
            childB = new Node(rightBox, right);

            childA.split(depth + 1);
            childB.split(depth + 1);
        }

    }
}
