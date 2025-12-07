package less.lgeo.material;

import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.nearZero;
import static less.lgeo.common.Vector3dUtils.randomUnitVector;

public class Lambertian implements Material {

    private final Vector3d albedo;

    public Lambertian(Vector3d albedo) {
        this.albedo = albedo;
    }

    @Override
    public ScatterResult scatter(Ray rayIn, HitRecord record) {
        Vector3d scatterDirection = new Vector3d(record.getNormal()).add(randomUnitVector());

        // Catch degenerate scatter direction
        if (nearZero(scatterDirection)) scatterDirection = record.getNormal();
        return new ScatterResult(albedo, new Ray(record.getPoint(), scatterDirection), true);
    }
}
