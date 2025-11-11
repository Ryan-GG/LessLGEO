package less.lgeo.primitive;

import less.lgeo.common.Color;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.lerp;
import static less.lgeo.common.Vector3dUtils.unitVector;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d at(double time) {
        return new Vector3d(origin).add(direction.mul(time));
    }

    public Color getColor() {


        Sphere sphere = new Sphere(new Vector3d(0, 0, -1), 0.5);
        HitRecord hitRecord = new HitRecord();
        if (sphere.hit(this, 0, 1, hitRecord)) {
            
            double t = hitRecord.getT();
            if (t > 0.0) {
                Vector3d normal = unitVector(at(t).sub(new Vector3d(0, 0, -1)));
                Vector3d colorVec = new Vector3d(normal.x() + 1, normal.y() + 1, normal.z() + 1).mul(0.5);

                return Color.builder()
                        .r(colorVec.x())
                        .g(colorVec.y())
                        .b(colorVec.z())
                        .isTransparent(false)
                        .build();
            }
        }

        Vector3d unitDirection = direction().lengthSquared() == 0
                ? new Vector3d(0, 0, 0)
                : direction().normalize();

        double a = 0.5 * (unitDirection.y() + 1.0);

        Vector3d colorOne = new Vector3d(1.0, 1.0, 1.0);
        Vector3d colorTwo = new Vector3d(0.5, 0.7, 1.0);

        // result = (1 - a) * colorOne + a * colorTwo
        Vector3d result = lerp(
                colorOne,
                colorTwo,
                a
        );

        return Color.builder()
                .r(result.x())
                .g(result.y())
                .b(result.z())
                .isTransparent(false)
                .build();
    }

}
