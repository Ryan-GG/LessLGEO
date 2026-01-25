package less.lgeo.material;

import less.lgeo.common.Ray;
import less.lgeo.common.Vector3dUtils;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import org.joml.Vector3d;

import java.util.Optional;
import java.util.Random;

public class Dielectric implements Material {

    private final Random random = new Random();
    // Refractive index in vacuum or air, or the ratio of the material's refractive index over
    // the refractive index of the enclosing media
    private final double refractionIndex;

    public Dielectric(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    private static double reflectance(double cosine, double refractionIndex) {
        // Use Schlick's approximation for reflectance.
        double r0 = (1 - refractionIndex) / (1 + refractionIndex);

        r0 = r0 * r0;

        return r0 + (1 - r0) * Math.pow(1 - cosine, 5);
    }

    @Override
    public Optional<ScatterResult> scatter(Ray rayIn, HitRecord record) {
        Vector3d attenuation = new Vector3d(1.0);
        double ri = record.frontFace() ? (1.0 / refractionIndex) : refractionIndex;

        Vector3d unitDirection = Vector3dUtils.unitVector(rayIn.direction());

        double cosAngle = Math.min(unitDirection.negate(new Vector3d()).dot(record.normal()), 1.0);
        double sinAngle = Math.sqrt(1.0 - cosAngle * cosAngle);

        boolean cannotRefract = ri * sinAngle > 1.0;
        Vector3d direction = cannotRefract || reflectance(cosAngle, ri) > random.nextDouble()
                ? Vector3dUtils.reflect(unitDirection, record.normal())
                : Vector3dUtils.refract(unitDirection, record.normal(), ri);

        Ray scatteredRay = new Ray(record.point(), direction);

        return Optional.of(new ScatterResult(attenuation, scatteredRay));
    }

}
