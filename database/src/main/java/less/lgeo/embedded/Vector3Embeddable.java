package less.lgeo.embedded;

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


    public Vector3 toVector3() {
        return new Vector3(getX(), getY(), getZ());
    }

}
