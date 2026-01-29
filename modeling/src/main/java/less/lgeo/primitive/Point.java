package less.lgeo.primitive;

import less.lgeo.common.Matrix;
import org.joml.Vector3d;

/**
 * Wrapper around {@link Vector3d}
 */
public record Point(Vector3d value) {
    
    public static Point of(double val) {
        return new Point(new Vector3d(val));
    }

    public static Point of(double x, double y, double z) {
        return new Point(new Vector3d(x, y, z));
    }

    /**
     * u' = a*u + b*v + c*w + x
     * v' = d*u + e*v + f*w + y
     * w' = g*u + h*v + i*w + z
     *
     * @param matrix {@link Matrix} that holds transformation
     * @return The resulting {@link Point} from the previous position with the
     * transformation applied.
     */
    public Point transform(Matrix matrix) {
        double x = value().x();
        double y = value().y();
        double z = value().z();

        double newX = matrix.a() * x + matrix.b() * y
                + matrix.c() * z + matrix.x();
        double newY = matrix.d() * x + matrix.e() * y
                + matrix.f() * z + matrix.y();
        double newZ = matrix.g() * x + matrix.h() * y
                + matrix.i() * z + matrix.z();

        return Point.of(newX, newY, newZ);
    }

    public double x() {
        return value().x();
    }

    public double y() {
        return value().y();
    }

    public double z() {
        return value().z();
    }

    public double get(int index) {
        return value().get(index);
    }

}
