package less.lgeo.tracer;

import less.lgeo.common.Color;
import org.joml.Vector3d;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d getPosition(double time) {
        return origin.add(direction.mul(time));
    }

    Color getColor() {
        return new Color(0, "Black", "0xffffff", false);
    }
}
