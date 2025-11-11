package less.lgeo.primitive;

public abstract class Hittable {

    public abstract boolean hit(Ray ray, double rayTMin, double rayTMax, HitRecord hitRecord);

}
