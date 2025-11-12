package less.lgeo.primitive;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3d;

@Setter
@Getter
public class HitRecord {

    private Vector3d point;
    private Vector3d normal;
    private double t;
    private boolean frontFace;

    void setFrontFace(Ray ray, Vector3d outwardNormal) {
        // Sets the hit record normal vector.
        // NOTE: the parameter `outward_normal` is assumed to have unit length.

        frontFace = new Vector3d(ray.direction()).dot(normal) < 0;
        normal = frontFace ? new Vector3d(outwardNormal) : new Vector3d(outwardNormal).negate();
    }
}
