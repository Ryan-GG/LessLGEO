package less.lgeo.hittable;

import less.lgeo.common.Interval;
import less.lgeo.common.Ray;

public abstract class Hittable {

    public abstract boolean hit(Ray ray, Interval rayTimeInterval, HitRecord hitRecord);

}
