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

    private static final int MAX_DEPTH = 2;
    private final Node root;

    public BoundingVolumeHierarchy(List<Triangle> triangles) {
        List<Vertex> vertices = triangles.stream()
                .flatMap(triangle -> getVertices(triangle).stream())
                .toList();
        BoundingBox boundingBox = new BoundingBox(vertices);
        Node root = new Node(boundingBox, triangles);
        root.split(root, 0);
        this.root = root;
    }

    public List<Line> getBoundingBoxes() {
        return getBoundBoxOfNode(getRoot());
    }

    private List<Line> getBoundBoxOfNode(Node node) {
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
        private Node childA = null;
        private Node childB = null;

        public Node(BoundingBox boundingBox, List<Triangle> triangles) {
            this.boundingBox = boundingBox;
            this.triangles = triangles;
        }

        public void split(Node parent, int depth) {
            if (depth == MAX_DEPTH) return;

            parent.childA = new Node();
            parent.childB = new Node();

            triangles.forEach(triangle -> {
                boolean inA = getCentroid(triangle).getX() < parent.boundingBox.getCenter().x();
                Node child = inA ? parent.childA : parent.childB;
                child.triangles.add(triangle);
                if (child.boundingBox == null) {
                    child.boundingBox = new BoundingBox(getVertices(triangle));
                } else {
                    child.boundingBox.growToInclude(triangle);
                }
            });

            if (!parent.childA.triangles.isEmpty()) split(parent.childA, depth + 1);
            if (!parent.childB.triangles.isEmpty()) split(parent.childB, depth + 1);
        }
    }
}
