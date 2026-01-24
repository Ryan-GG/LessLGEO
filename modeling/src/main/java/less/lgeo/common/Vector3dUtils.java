package less.lgeo.common;

import org.joml.Vector3d;

import java.util.Random;

import static java.lang.Math.sqrt;

/**
 * FIXME, i honestly kind of hate this an wonder if its possible to just write my own vec3 class.
 * Maybe just essentially copy vector3d then add these methods into it.
 */
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
        return vector.div(vector.length(), new Vector3d());
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

    public static Vector3d randomVec3d(double min, double max) {
        Random random = new Random();
        return new Vector3d(random.nextDouble(min, max), random.nextDouble(min, max), random.nextDouble(min, max));
    }

    public static Vector3d randomUnitVectorInDisk() {
        Random random = new Random();
        while (true) {
            Vector3d p = new Vector3d(random.nextDouble(-1, 1), random.nextDouble(-1, 1), 0);
            if (p.lengthSquared() < 1)
                return p;
        }
    }
    
    public static boolean nearZero(Vector3d vector) {
        // Return true if the vector is close to zero in all dimensions.
        double s = 1e-8;
        return (Math.abs(vector.x()) < s) && (Math.abs(vector.y()) < s) && (Math.abs(vector.z()) < s);
    }

    public static Vector3d reflect(Vector3d vector, Vector3d normal) {
        return vector.sub(normal.mul(vector.dot(normal) * 2, new Vector3d()), new Vector3d());
    }

    public static Vector3d refract(Vector3d uv, Vector3d normal, double eTaiOverEtat) {
        double cosTheta = Math.min(uv.negate(new Vector3d()).dot(normal), 1.0);
        Vector3d rOutPerpendicular = uv.add(normal.mul(cosTheta, new Vector3d()), new Vector3d()).mul(eTaiOverEtat);
        Vector3d rOutParallel = normal.mul(-Math.sqrt(Math.abs(1.0 - rOutPerpendicular.lengthSquared())), new Vector3d());
        return rOutPerpendicular.add(rOutParallel);
    }
}
