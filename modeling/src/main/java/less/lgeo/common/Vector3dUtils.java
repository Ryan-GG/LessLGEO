package less.lgeo.common;

import org.joml.Vector3d;

public class Vector3dUtils {

    /**
     * u' = a*u + b*v + c*w + x
     * v' = d*u + e*v + f*w + y
     * w' = g*u + h*v + i*w + z
     *
     * @param matrix {@link Matrix} that holds transformation
     * @return The resulting {@link Vector3d} from the previous position with the
     * transformation applied.
     */
    public static Vector3d transform(Vector3d vector, Matrix matrix) {
        double x = vector.x;
        double y = vector.y;
        double z = vector.z;

        double newX = matrix.a() * x + matrix.b() * y
                + matrix.c() * z + matrix.x();
        double newY = matrix.d() * x + matrix.e() * y
                + matrix.f() * z + matrix.y();
        double newZ = matrix.g() * x + matrix.h() * y
                + matrix.i() * z + matrix.z();

        return new Vector3d(newX, newY, newZ);
    }

    public static Vector3d unitVector(Vector3d vector) {
        return vector.div(vector.length());
    }
}
