package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joml.Vector3d;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Vector3dEmbeddable {

    private double x;
    private double y;
    private double z;

    public Vector3dEmbeddable(Vector3d vertex) {
        this.x = vertex.x;
        this.y = vertex.y;
        this.z = vertex.z;
    }


    public Vector3d toVector3d() {
        return new Vector3d(getX(), getY(), getZ());
    }

}
