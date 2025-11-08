package less.lgeo.tracer;

import less.lgeo.common.Color;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static less.lgeo.common.Vector3dUtils.lerp;
import static less.lgeo.common.Vector3dUtils.unitVector;

public record Ray(Vector3d origin, Vector3d direction) {

    private static final Logger logger = LoggerFactory.getLogger(Ray.class);

    public Vector3d getPosition(double time) {
        return new Vector3d(origin).add(direction.mul(time));
    }

    public Color getColor() {

        double t = hitSphere(new Vector3d(0, 0, -1), 0.5);
        if (t > 0.0) {
            Vector3d normal = unitVector(getPosition(t).sub(new Vector3d(0, 0, -1)));
            Vector3d colorVec = new Vector3d(normal.x() + 1, normal.y() + 1, normal.z() + 1).mul(0.5);

            return Color.builder()
                    .r(colorVec.x())
                    .g(colorVec.y())
                    .b(colorVec.z())
                    .isTransparent(false)
                    .build();
        }

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
    
    public double hitSphere(Vector3d center, double radius) {
        //FIXME, I'm still not sure what OC means
        Vector3d oc = new Vector3d(center).sub(origin);

        double a = new Vector3d(direction).lengthSquared();
        double h = new Vector3d(direction).dot(oc);
        double c = oc.lengthSquared() - radius * radius;
        double discriminant = h * h - a * c;

        if (discriminant < 0) {
            return -1.0;
        } else {
            return (h - Math.sqrt(discriminant)) / a;
        }
    }
}
