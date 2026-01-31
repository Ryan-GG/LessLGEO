package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import less.lgeo.primitive.Point;
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
public class PointEmbeddable {

    private double x;
    private double y;
    private double z;

    public PointEmbeddable(Vector3d point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    public PointEmbeddable(Point point) {
        this.x = point.x();
        this.y = point.y();
        this.z = point.z();
    }

    public Point toDomain() {
        return Point.of(getX(), getY(), getZ());
    }

}
