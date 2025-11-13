package less.lgeo.hittable;

import less.lgeo.primitive.Ray;

public abstract class Hittable {

    public abstract boolean hit(Ray ray, double rayTMin, double rayTMax, HitRecord hitRecord);

}
