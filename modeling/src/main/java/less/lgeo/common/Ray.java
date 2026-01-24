package less.lgeo.common;

import org.joml.Vector3d;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d at(double time) {
        return origin.add(direction.mul(time, new Vector3d()), new Vector3d());
    }
}
