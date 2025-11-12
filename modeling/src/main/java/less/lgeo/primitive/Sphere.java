package less.lgeo.primitive;

import lombok.Getter;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.unitVector;

@Getter
public class Sphere extends Hittable {

    private final Vector3d center;
    private final double radius;

    public Sphere(Vector3d center, double radius) {
        this.center = center;
        this.radius = Math.max(0, radius);
    }

    @Override
    public boolean hit(Ray ray, double rayTMin, double rayTMax, HitRecord hitRecord) {
        //FIXME, I'm still not sure what OC means
        Vector3d oc = new Vector3d(center).sub(ray.origin());

        double a = new Vector3d(ray.direction()).lengthSquared();
        double h = new Vector3d(ray.direction()).dot(oc);
        double c = oc.lengthSquared() - radius * radius;
        double discriminant = h * h - a * c;

        if (discriminant < 0)
            return false;

        double sqrtd = Math.sqrt(discriminant);

        // Find the nearest root that lies in the acceptable range.
        double root = (h - sqrtd) / a;
        if (root <= rayTMin || rayTMax <= root) {
            root = (h + sqrtd) / a;
            if (root <= rayTMin || rayTMax <= root)
                return false;
        }

        hitRecord.setPoint(ray.at(root));
        Vector3d outwardNormal = unitVector(new Vector3d(ray.at(root)).sub(center).div(radius));
        hitRecord.setFrontFace(ray, outwardNormal);
        hitRecord.setT(root);

        return true;
    }
}
