package less.lgeo.primitive;

import org.joml.Vector3d;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d at(double time) {
        return new Vector3d(origin).add(direction.mul(time));
    }
}
