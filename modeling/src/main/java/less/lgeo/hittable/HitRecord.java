package less.lgeo.hittable;

import less.lgeo.common.Ray;
import less.lgeo.material.Material;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3d;

//FIXME, Fix this mutability by reference
@Setter
@Getter
public class HitRecord {

    private Vector3d point;
    private Vector3d normal;
    private double t;
    private boolean frontFace;
    private Material material;

    public void setFrontFace(Ray ray, Vector3d outwardNormal) {
        // Sets the hit record normal vector.
        // NOTE: the parameter `outward_normal` is assumed to have unit length.

        frontFace = new Vector3d(ray.direction()).dot(outwardNormal) < 0;
        normal = frontFace ? new Vector3d(outwardNormal) : outwardNormal.negate(new Vector3d());
    }
}
