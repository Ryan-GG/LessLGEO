package less.lgeo.tracer;

import less.lgeo.common.Color;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.unitVector;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d getPosition(double time) {
        return origin.add(direction.mul(time));
    }

    Color getColor() {
        Vector3d unitDirection = unitVector(direction());
        double a = 0.5 * (unitDirection.y() + 1.0);
        Vector3d colorOne = new Vector3d(1, 1, 1);
        Vector3d colorTwo = new Vector3d(0.5, 0.7, 1.0);
        Vector3d result = colorOne.mul(1.0 - a).add(colorTwo.mul(a));

        return Color.builder()
                .r(result.x())
                .g(result.y())
                .b(result.z())
                .isTransparent(false)
                .build();
    }
}
