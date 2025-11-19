package less.lgeo.hittable;

import less.lgeo.common.Interval;
import less.lgeo.primitive.Ray;

public abstract class Hittable {

    public abstract boolean hit(Ray ray, Interval rayTimeInterval, HitRecord hitRecord);

}
