package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.HittableList;
import org.joml.Vector3d;

import static less.lgeo.common.Vector3dUtils.lerp;
import static less.lgeo.common.Vector3dUtils.unitVector;

public record Ray(Vector3d origin, Vector3d direction) {

    public Vector3d at(double time) {
        return new Vector3d(origin).add(direction.mul(time));
    }

    public Color getColor(HittableList world) {
        HitRecord rec = new HitRecord();
        boolean hasRayHitSurface = world.hit(this, 0, Double.POSITIVE_INFINITY, rec);

        if (hasRayHitSurface) {
            Vector3d normal = unitVector(rec.getNormal());
            Vector3d colorVec = normal.add(1, 1, 1).mul(0.5);

            return Color.builder()
                    .r(colorVec.x())
                    .g(colorVec.y())
                    .b(colorVec.z())
                    .isTransparent(false)
                    .build();
        }

        Vector3d unitDirection = unitVector(direction());

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
