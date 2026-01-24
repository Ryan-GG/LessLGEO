package less.lgeo.material;

import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;

public interface Material {

    ScatterResult scatter(Ray rayIn, HitRecord record);
}
