package less.lgeo.material;

import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import org.joml.Vector3d;

import java.util.Optional;

import static less.lgeo.common.Vector3dUtils.nearZero;
import static less.lgeo.common.Vector3dUtils.randomUnitVector;

public class Lambertian implements Material {

    private final Vector3d albedo;

    public Lambertian(Vector3d albedo) {
        this.albedo = albedo;
    }

    @Override
    public Optional<ScatterResult> scatter(Ray rayIn, HitRecord record) {
        Vector3d scatterDirection = record.normal().add(randomUnitVector(), new Vector3d());

        // Catch degenerate scatter direction
        if (nearZero(scatterDirection)) scatterDirection = record.normal();
        return Optional.of(new ScatterResult(albedo, new Ray(record.point(), scatterDirection)));
    }
}
