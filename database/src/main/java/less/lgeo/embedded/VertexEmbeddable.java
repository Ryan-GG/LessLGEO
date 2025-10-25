package less.lgeo.embedded;

import static less.lgeo.common.Vector3Utils.toVector3;

import jakarta.persistence.Embeddable;
import less.lgeo.common.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Vector3Embeddable {

  private double x;
  private double y;
  private double z;

  public Vector3Embeddable(Vector3 vertex) {
    this.x = vertex.getX();
    this.y = vertex.getY();
    this.z = vertex.getZ();
  }


  public static Vector3 toGpb(Vector3Embeddable vertexEmbeddable) {
    return toVector3(vertexEmbeddable.getX(), vertexEmbeddable.getY(), vertexEmbeddable.getZ());
  }

}
