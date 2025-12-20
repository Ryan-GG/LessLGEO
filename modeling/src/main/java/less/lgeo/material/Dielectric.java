package less.lgeo.material;

import less.lgeo.common.Vector3dUtils;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import org.joml.Vector3d;

import java.util.Random;

public class Dielectric implements Material{

    // Refractive index in vacuum or air, or the ratio of the material's refractive index over
    // the refractive index of the enclosing media
    private final double refractionIndex;
    //FIXME, For vector utils refactor this will be moved into there on the refactor so we don't have to worry about this and can provide seeds
    private static final Random random = new Random();
        public Dielectric(double refractionIndex) {
            this.refractionIndex = refractionIndex;
        }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        Vector3d attenuation = new Vector3d(1.0);
        double ri = record.isFrontFace() ? (1.0/refractionIndex) : refractionIndex;

        Vector3d unitDirection = Vector3dUtils.unitVector(rayIn.direction());

        double cosAngle = Math.min( new Vector3d(unitDirection).negate().dot(record.getNormal()),1.0);
        double sinAngle = Math.sqrt(1.0 - cosAngle * cosAngle);

        boolean cannotRefract = ri * sinAngle > 1.0;
        Vector3d direction = cannotRefract || reflectance(cosAngle, ri) > random.nextDouble()
                ? Vector3dUtils.reflect(unitDirection, record.getNormal())
                : Vector3dUtils.refract(unitDirection, record.getNormal(), ri);

        Ray scattered = new Ray(record.getPoint(), direction);

        return new ScatterResult(attenuation, scattered, true);
    }

    private static double reflectance( double cosine, double refractionIndex) {
        // Use Schlick's approximation for reflectance.
        double r0 = (1 - refractionIndex) / (1 + refractionIndex);

        r0 = r0 * r0;

        return r0 + ( 1 - r0 )*Math.pow(1 - cosine, 5);
    }

}
