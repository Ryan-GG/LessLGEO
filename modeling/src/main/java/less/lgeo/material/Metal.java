package less.lgeo.material;

import less.lgeo.common.Color;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import org.joml.Vector3d;

import java.util.Optional;

import static less.lgeo.common.Vector3dUtils.*;

public class Metal implements Material {

    private final Vector3d albedo;
    private final double fuzz;

    public Metal(Color albedo, double fuzz) {
        this.albedo = albedo.toVector3d();
        this.fuzz = Math.min(fuzz, 1);
    }


    @Override
    public Optional<ScatterResult> scatter(Ray rayIn, HitRecord record) {
        Vector3d reflected = unitVector(reflect(rayIn.direction(), record.normal()));
        reflected = randomUnitVector().mul(fuzz).add(reflected);
        Ray scattered = new Ray(record.point(), reflected);

        if (0 < scattered.direction().dot(record.normal())) {
            return Optional.of(new ScatterResult(albedo, scattered));
        }

        return Optional.empty();
    }
}
