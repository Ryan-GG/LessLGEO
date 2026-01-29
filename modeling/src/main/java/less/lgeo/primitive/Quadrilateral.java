package less.lgeo.primitive;

import less.lgeo.Pair;
import less.lgeo.common.*;
import less.lgeo.hittable.HitRecord;
import less.lgeo.hittable.Hittable;
import less.lgeo.material.Material;
import org.joml.Vector3d;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static less.lgeo.common.Vector3dUtils.unitVector;

public class Quadrilateral implements Hittable {

    public static final LineType type = LineType.QUADRILATERAL;

    private final Color color;
    private final Point p1;
    private final Point p2;
    private final Point p3;
    private final Point p4;

    private final Material material;

    private final Vector3d normal;

    //Vectors defining edges from P1
    private final Vector3d u;
    private final Vector3d v;
    
    private final double d; // plane constant
    private final Vector3d w; // helper vector


    public Quadrilateral(
            Color color,
            Point p1,
            Point p2,
            Point p3,
            Point p4
    ) {
        this.color = color;
        this.material = Material.fromColor(color);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;

        this.v = p2.value().sub(p1.value(), new Vector3d());
        this.u = p4.value().sub(p1.value(), new Vector3d());

        Vector3d n = u.cross(v, new Vector3d());
        this.normal = unitVector(n);
        this.d = normal.dot(p1.value());
        this.w = n.div(n.dot(n), new Vector3d());
    }

    public Color color() {
        return color;
    }

    public Point p1() {
        return p1;
    }

    public Point p2() {
        return p2;
    }

    public Point p3() {
        return p3;
    }

    public Point p4() {
        return p4;
    }

    public List<Point> getVertices() {
        return List.of(p1, p2, p3, p4);
    }

    public Quadrilateral transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {

        return new Quadrilateral(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3),
                transformationMatrix.map(p4::transform).orElse(p4));
    }

    public List<Triangle> tessellate() {
        Triangle bottomLeft = new Triangle(color, p1, p2, p4);
        Triangle topRight = new Triangle(color, p2, p3, p4);
        return List.of(bottomLeft, topRight);
    }

    /**
     * @param ray             Ray being cast
     * @param rayTimeInterval time of ray during cast interval
     * @return Empty if nothing was hit, otherwise return the {@link HitRecord} which defines what was hit
     */
    @Override
    public Optional<HitRecord> hit(Ray ray, Interval rayTimeInterval) {
        double denominator = normal.dot(ray.direction());

        // No hit if the ray is parallel to the plane.
        if (Math.abs(denominator) < 1e-8)
            return Optional.empty();

        // Return false if the hit point parameter t is outside the ray interval.
        double t = (d - normal.dot(ray.origin().value())) / denominator;
        if (!rayTimeInterval.contains(t))
            return Optional.empty();

        // Determine if the hit point lies within the planar shape using its plane coordinates.
        Point intersection = ray.at(t);
        Vector3d planarHitPointVector = intersection.value().sub(p1.value(), new Vector3d());
        double alpha = w.dot(planarHitPointVector.cross(v, new Vector3d()));
        double beta = w.dot(u.cross(planarHitPointVector, new Vector3d()));

        if (!isInterior(alpha, beta))
            return Optional.empty();

        // Ray hits the 2D shape; set the rest of the hit record and return true.

        Pair<Vector3d, Boolean> res = HitRecord.getOutwardNormal(ray, normal);
        HitRecord record = new HitRecord(intersection, t, res.first(), res.second(), material);

        return Optional.of(record);
    }

    private boolean isInterior(double a, double b) {
        Interval unitInterval = Interval.of(0, 1);

        // Given the hit point in plane coordinates, return false if it is outside the
        // primitive, otherwise set the hit record UV coordinates and return true.
        return unitInterval.contains(a) && unitInterval.contains(b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quadrilateral that = (Quadrilateral) o;
        return Objects.equals(color, that.color)
                && Objects.equals(p1, that.p1)
                && Objects.equals(p2, that.p2)
                && Objects.equals(p3, that.p3)
                && Objects.equals(p4, that.p4);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, p1, p2, p3, p4);
    }

    @Override
    public String toString() {
        return "Quadrilateral{" +
                "p4=" + p4 +
                ", color=" + color +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                '}';
    }
}
