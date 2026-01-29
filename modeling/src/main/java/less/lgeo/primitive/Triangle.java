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

public class Triangle implements Hittable {


    public static final LineType type = LineType.TRIANGLE;

    private final Color color;

    private final Point p1;
    private final Point p2;
    private final Point p3;

    private final Material material;

    private final Vector3d normal;

    //Vectors defining edges from P1
    private final Vector3d u;
    private final Vector3d v;

    private final double d;    // plane constant
    private final Vector3d w;  // barycentric helper vector

    public Triangle(Color color, Point p1, Point p2, Point p3) {
        this.color = color;
        this.material = Material.fromColor(color);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        this.u = p2.value().sub(p1.value(), new Vector3d());
        this.v = p3.value().sub(p1.value(), new Vector3d());

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

    public Point getCentroid() {
        double xCentroid = (p1.x() + p2.x() + p3.x()) / 3;
        double yCentroid = (p1.y() + p2.y() + p3.y()) / 3;
        double zCentroid = (p1.z() + p2.z() + p3.z()) / 3;

        return Point.of(xCentroid, yCentroid, zCentroid);
    }

    public List<Point> getVertices() {
        return List.of(p1, p2, p3);
    }

    public Triangle transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        return new Triangle(
                color.inheritColor(inheritedColor),
                transformationMatrix.map(p1::transform).orElse(p1),
                transformationMatrix.map(p2::transform).orElse(p2),
                transformationMatrix.map(p3::transform).orElse(p3));
    }

    @Override
    public Optional<HitRecord> hit(Ray ray, Interval rayTimeInterval) {
        double denominator = normal.dot(ray.direction());

        if (Math.abs(denominator) < 1e-8) return Optional.empty();

        double t = (d - normal.dot(ray.origin().value())) / denominator;

        if (!rayTimeInterval.contains(t)) return Optional.empty();

        Point intersection = ray.at(t);
        Vector3d p = intersection.value().sub(p1.value(), new Vector3d());
        double u = w.dot(p.cross(v, new Vector3d()));
        double v = w.dot(this.u.cross(p, new Vector3d()));

        if (!isInterior(u, v)) return Optional.empty();

        Pair<Vector3d, Boolean> res = HitRecord.getOutwardNormal(ray, normal);
        HitRecord hitRecord = new HitRecord(
                intersection,
                t,
                res.first(),
                res.second(),
                material
        );
        return Optional.of(hitRecord);
    }

    private boolean isInterior(double u, double v) {
        return (!(u < 0)) && (!(v < 0)) && (!(u + v > 1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(color, triangle.color)
                && Objects.equals(p1, triangle.p1)
                && Objects.equals(p2, triangle.p2)
                && Objects.equals(p3, triangle.p3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, p1, p2, p3);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                ", color=" + color +
                '}';
    }
}
