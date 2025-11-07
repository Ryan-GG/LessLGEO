package less.lgeo.tracer;

import less.lgeo.common.Color;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static less.lgeo.common.Vector3dUtils.lerp;

public record Ray(Vector3d origin, Vector3d direction) {

    private static final Logger logger = LoggerFactory.getLogger(Ray.class);

    public Vector3d getPosition(double time) {
        return origin.add(direction.mul(time));
    }

    Color getColor() {

        Vector3d unitDirection = direction().lengthSquared() == 0
                ? new Vector3d(0, 0, 0)
                : direction().normalize();

        double a = 0.5 * (unitDirection.y() + 1.0);

        Vector3d colorOne = new Vector3d(1.0, 1.0, 1.0);
        Vector3d colorTwo = new Vector3d(0.5, 0.7, 1.0);

        // result = (1 - a) * colorOne + a * colorTwo
        Vector3d result = lerp(
                colorOne,
                colorTwo,
                a
        );
        
        return Color.builder()
                .r(result.x())
                .g(result.y())
                .b(result.z())
                .isTransparent(false)
                .build();
    }
}
