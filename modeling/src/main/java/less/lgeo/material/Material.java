package less.lgeo.material;

import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.ScatterResult;
import less.lgeo.primitive.Ray;

public interface Material {

    ScatterResult scatter(Ray rayIn, HitRecord record);
}
