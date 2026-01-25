package less.lgeo.hittable;

import less.lgeo.material.Material;
import less.lgeo.primitive.Point;
import org.joml.Vector3d;

public record HitRecord(Point point, Vector3d normal, double time, boolean frontFace, Material material) {
}
