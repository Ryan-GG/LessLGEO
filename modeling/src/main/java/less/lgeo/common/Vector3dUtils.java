package less.lgeo.common;

import org.joml.Vector3d;

import java.util.Random;

import static java.lang.Math.sqrt;

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
        if (vector.length() == 0) return new Vector3d(0);
        return new Vector3d(vector).div(vector.length());
    }

    /**
     * Rejection approach for creating a unit vector
     * reject any unit vector outside the unit circle, if exists inside then accept and normal to a unit vector
     *
     * @return a randomly created vector that is normalized to a unit vector
     */
    public static Vector3d randomUnitVector() {
        while (true) {
            Vector3d p = randomVec3d(-1, 1);
            double lensq = p.lengthSquared();
            /*
             * Sadly, we have a small floating-point abstraction leak to deal with.
             * Since floating-point numbers have finite precision, a very small value can underflow to zero when squared.
             * So if all three coordinates are small enough (that is, very near the center of the sphere),
             * the norm of the vector will be zero, and thus normalizing will yield the bogus vector [±∞,±∞,±∞].
             * To fix this, we'll also reject points that lie inside this “black hole” around the center.
             * With double precision (64-bit floats), we can safely support values greater than 10−160.
             */
            if (1e-160 < lensq && lensq <= 1)
                return p.div(sqrt(lensq));
        }
    }

    public static Vector3d lerp(Vector3d v1, Vector3d v2, double t) {
        return new Vector3d(v1).mul(1.0 - t).add(new Vector3d(v2).mul(t));
    }

    public static Vector3d randomVec3d() {
        Random random = new Random();
        return new Vector3d(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public static Vector3d randomVec3d(double min, double max) {
        Random random = new Random();
        return new Vector3d(random.nextDouble(min, max), random.nextDouble(min, max), random.nextDouble(min, max));
    }

    public static Vector3d randomOnHemisphere(Vector3d normal) {
        Vector3d onUnitSphere = randomUnitVector();
        if (onUnitSphere.dot(normal) > 0.0) // In the same hemisphere as the normal
            return onUnitSphere;
        else
            return onUnitSphere.negate();
    }

}
