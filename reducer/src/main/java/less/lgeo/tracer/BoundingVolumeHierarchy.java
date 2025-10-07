package less.lgeo.tracer;


import static less.lgeo.primitive.TriangleUtils.getCentroid;

import java.util.ArrayList;
import java.util.List;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.Triangle;
import less.lgeo.primitive.TriangleUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class BoundingVolumeHierarchy {

  private static final int MAX_DEPTH = 20;

  private final Node root;

  private BoundingVolumeHierarchy(List<Triangle> triangles) {
    List<Vertex> vertices = triangles.stream()
        .flatMap(triangle -> TriangleUtils.getVertices(triangle).stream())
        .toList();
    BoundingBox boundingBox = new BoundingBox(vertices);
    Node root = new Node(boundingBox, triangles);
    root.split(root, 0);
    this.root = root;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  private static class Node {

    private BoundingBox boundingBox = new BoundingBox();
    private List<Triangle> triangles = new ArrayList<>();
    private Node childA = null;
    private Node childB = null;

    public Node(BoundingBox boundingBox, List<Triangle> triangles) {
      this.boundingBox = boundingBox;
      this.triangles = triangles;
    }

    public void split(Node parent, int depth) {
      if (depth == MAX_DEPTH) {
        return;
      }

      triangles.forEach(triangle ->
      {
        boolean inA = getCentroid(triangle).getX() < parent.boundingBox.getCenter().x;
        Node child = inA ? parent.childA : parent.childB;
        child.triangles.add(triangle);
        child.boundingBox.growToInclude(triangle);
      });
      parent.childA = new Node();
      parent.childB = new Node();
      split(parent.childA, depth + 1);
      split(parent.childB, depth + 1);
    }
  }
}
