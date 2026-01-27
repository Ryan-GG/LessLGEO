package less.lgeo.primitive;

import less.lgeo.Pair;
import less.lgeo.common.Interval;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.Hittable;
import less.lgeo.material.Material;
import org.joml.Vector3d;

import java.util.Optional;

public record Sphere(Vector3d center, double radius, Material material) implements Hittable {

    public Sphere(Vector3d center, double radius, Material material) {
        this.center = center;
        this.radius = Math.max(0, radius);
        this.material = material;
    }

    @Override
    public Optional<HitRecord> hit(Ray ray, Interval rayTimeInterval) {
        Vector3d oc = center.sub(ray.origin().value(), new Vector3d());

        double a = ray.direction().lengthSquared();
        double h = ray.direction().dot(oc);
        double c = oc.lengthSquared() - radius * radius;

        double discriminant = h * h - a * c;

        if (discriminant < 0)
            return Optional.empty();

        double sqrtd = Math.sqrt(discriminant);

        // Find nearest valid root
        double root = (h - sqrtd) / a;
        if (!rayTimeInterval.surrounds(root)) {
            root = (h + sqrtd) / a;
            if (!rayTimeInterval.surrounds(root))
                return Optional.empty();
        }


        Point p = ray.at(root);

        Vector3d outwardNormal = p.value().sub(center, new Vector3d()).div(radius);
        Pair<Vector3d, Boolean> res = HitRecord.getOutwardNormal(ray, outwardNormal);

        return Optional.of(new HitRecord(
                        ray.at(root),
                        root,
                        res.first(),
                        res.second(),
                        material
                )
        );
    }
}
