package less.lgeo.material;

import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.reflect;

public class Metal implements Material {

    private final Vector3d albedo;

    public Metal(Vector3d albedo) {
        this.albedo = albedo;
    }


    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        Vector3d reflected = reflect(rayIn.direction(), record.getNormal());
        return new ScatterResult(albedo, new Ray(record.getPoint(), reflected), true);
    }
}
