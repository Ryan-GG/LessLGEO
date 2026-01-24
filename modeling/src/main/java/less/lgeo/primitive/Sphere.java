package less.lgeo.primitive;

import less.lgeo.common.Interval;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.Hittable;
import less.lgeo.material.Material;
import lombok.Getter;
import org.joml.Vector3d;

@Getter
public class Sphere extends Hittable {

    private final Vector3d center;
    private final double radius;
    private final Material material;

    public Sphere(Vector3d center, double radius, Material material) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
    }

    @Override
    public boolean hit(Ray ray, Interval rayTimeInterval, HitRecord hitRecord) {
        Vector3d oc = center.sub(ray.origin(), new Vector3d());

        double a = ray.direction().lengthSquared();
        double h = ray.direction().dot(oc);
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = h * h - a * c;

        if (discriminant < 0)
            return false;

        double sqrtd = Math.sqrt(discriminant);

        // Find nearest valid root
        double root = (h - sqrtd) / a;
        if (!rayTimeInterval.surrounds(root)) {
            root = (h + sqrtd) / a;
            if (!rayTimeInterval.surrounds(root))
                return false;
        }

        hitRecord.setT(root);

        Vector3d p = ray.at(root);
        hitRecord.setPoint(p);

        Vector3d outwardNormal = p.sub(center, new Vector3d()).div(radius);

        hitRecord.setFrontFace(ray, outwardNormal);
        hitRecord.setMaterial(material);

        return true;
    }
}
