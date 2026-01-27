package less.lgeo.hittable;

import less.lgeo.Pair;
import less.lgeo.common.Ray;
import less.lgeo.material.Material;
import less.lgeo.primitive.Point;
import org.joml.Vector3d;

public record HitRecord(Point point, double time, Vector3d normal, boolean frontFace, Material material) {

    public static Pair<Vector3d, Boolean> getOutwardNormal(Ray ray, Vector3d outwardNormal) {
        boolean frontFace = ray.direction().dot(outwardNormal) < 0;
        Vector3d normal = frontFace ? new Vector3d(outwardNormal) : outwardNormal.negate(new Vector3d());
        return new Pair<>(normal, frontFace);
    }
}
