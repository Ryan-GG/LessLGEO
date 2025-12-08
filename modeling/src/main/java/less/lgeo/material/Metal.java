package less.lgeo.material;

import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.*;

public class Metal implements Material {

    private final Vector3d albedo;
    private final double fuzz;

    //FIXME, albedo should be a color, and i should have the ability to go from Color to Vec3
    public Metal(Vector3d albedo, double fuzz) {
        this.albedo = albedo;
        this.fuzz = Math.min(fuzz, 1);
    }


    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        Vector3d reflected = unitVector(reflect(rayIn.direction(), record.getNormal()));
        reflected = randomUnitVector().mul(fuzz).add(reflected);
        Ray scattered = new Ray(record.getPoint(), reflected);
        return new ScatterResult(albedo, scattered, 0 < scattered.direction().dot(record.getNormal()));
    }
}
