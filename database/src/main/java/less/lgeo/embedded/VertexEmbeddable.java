package less.lgeo.embedded;

import static less.lgeo.common.VertexUtils.toVertex;

import jakarta.persistence.Embeddable;
import less.lgeo.common.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class VertexEmbeddable {

  private double x;
  private double y;
  private double z;

  public VertexEmbeddable(Vertex vertex) {
    this.x = vertex.getX();
    this.y = vertex.getY();
    this.z = vertex.getZ();
  }


  public static Vertex toGpb(VertexEmbeddable vertexEmbeddable) {
    return toVertex(vertexEmbeddable.getX(), vertexEmbeddable.getY(), vertexEmbeddable.getZ());
  }

}
