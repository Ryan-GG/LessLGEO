package less.lgeo.material;

import less.lgeo.common.Vector3dUtils;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class Dielectric implements Material{

    // Refractive index in vacuum or air, or the ratio of the material's refractive index over
    // the refractive index of the enclosing media
    private final double refractionIndex;
        public Dielectric(double refractionIndex) {
            this.refractionIndex = refractionIndex;
        }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        Vector3d attenuation = new Vector3d(1.0);
        double ri = record.isFrontFace() ? (1.0/refractionIndex) : refractionIndex;

        Vector3d unitDirection = Vector3dUtils.unitVector(rayIn.direction());
        Vector3d refracted = Vector3dUtils.refract(unitDirection, record.getNormal(), ri);

        Ray scattered = new Ray(record.getPoint(), refracted);

        return new ScatterResult(attenuation, scattered, true);
    }

}
