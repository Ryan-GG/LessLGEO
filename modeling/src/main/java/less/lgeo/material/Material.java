package less.lgeo.material;

import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;

import java.util.Optional;

public interface Material {

    Optional<ScatterResult> scatter(Ray rayIn, HitRecord record);
}
