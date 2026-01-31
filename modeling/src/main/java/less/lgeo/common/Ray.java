package less.lgeo.common;

import less.lgeo.primitive.Point;
import org.joml.Vector3d;

public record Ray(Point origin, Vector3d direction) {

    public Point at(double time) {
        return new Point(origin.value().add(direction.mul(time, new Vector3d()), new Vector3d()));
    }
}
