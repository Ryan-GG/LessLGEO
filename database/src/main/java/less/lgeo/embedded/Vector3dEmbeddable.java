package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector3d;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Vector3dEmbeddable {

    private double x;
    private double y;
    private double z;

    public Vector3dEmbeddable(Vector3d vertex) {
        this.x = vertex.x;
        this.y = vertex.y;
        this.z = vertex.z;
    }

    public Vector3d toDomain() {
        return new Vector3d(getX(), getY(), getZ());
    }

}
