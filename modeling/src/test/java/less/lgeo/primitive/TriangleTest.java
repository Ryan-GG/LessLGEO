package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.Interval;
import less.lgeo.common.Matrix;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import less.lgeo.util.ModelTestUtils;
import org.joml.Vector3d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {

    private Color redColor;
    private Point p1, p2, p3;
    private Triangle triangle;

    @BeforeEach
    void setUp() {
        redColor = ModelTestUtils.RED;
        p1 = Point.of(0, 0, 0);
        p2 = Point.of(1, 0, 0);
        p3 = Point.of(0, 0, 1);
        triangle = new Triangle(redColor, p1, p2, p3);
    }

    @Nested
    @DisplayName("Centroid Tests")
    class CentroidTests {

        @Test
        @DisplayName("Should calculate correct centroid")
        void shouldCalculateCorrectCentroid() {
            Point centroid = triangle.getCentroid();
            assertEquals(1.0 / 3.0, centroid.x());
            assertEquals(0.0, centroid.y());
            assertEquals(1.0 / 3.0, centroid.z());
        }

        @Test
        @DisplayName("Should calculate centroid for arbitrary triangle")
        void shouldCalculateCentroidForArbitraryTriangle() {
            Triangle tri = new Triangle(redColor,
                    Point.of(0, 0, 0),
                    Point.of(3, 0, 0),
                    Point.of(0, 0, 3));
            Point centroid = tri.getCentroid();
            assertEquals(1.0, centroid.x());
            assertEquals(0.0, centroid.y());
            assertEquals(1.0, centroid.z());
        }
    }

    @Nested
    @DisplayName("Ray Intersection Tests")
    class RayIntersectionTests {

        @Test
        @DisplayName("Should hit triangle at centroid from above")
        void shouldHitTriangleCentroidFromAbove() {
            Point centroid = triangle.getCentroid();
            Ray ray = new Ray(
                    Point.of(centroid.x(), -1, centroid.z()),
                    new Vector3d(0, 1, 0)
            );
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent(), "Ray should hit triangle centroid");
            HitRecord record = hit.get();
            assertEquals(1.0 / 3.0, record.point().x());
            assertEquals(0, record.point().y());
            assertEquals(1.0 / 3.0, record.point().z());
        }

        @Test
        @DisplayName("Should hit triangle at p1 vertex")
        void shouldHitTriangleAtP1() {
            Ray ray = new Ray(Point.of(0, -1, 0), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent(), "Ray should hit at p1 vertex (0,0,0)");
            assertEquals(0, hit.get().point().x());
            assertEquals(0, hit.get().point().y());
            assertEquals(0, hit.get().point().z());
        }

        @Test
        @DisplayName("Should hit triangle at p2 vertex")
        void shouldHitTriangleAtP2() {
            Ray ray = new Ray(Point.of(1, -1, 0), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent(), "Ray should hit at p2 vertex (1,0,0)");
            assertEquals(1, hit.get().point().x());
            assertEquals(0, hit.get().point().y());
            assertEquals(0, hit.get().point().z());
        }

        @Test
        @DisplayName("Should hit triangle at p3 vertex")
        void shouldHitTriangleAtP3() {
            Ray ray = new Ray(Point.of(0, -1, 1), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent(), "Ray should hit at p3 vertex (0,0,1)");
            assertEquals(0, hit.get().point().x());
            assertEquals(0, hit.get().point().y());
            assertEquals(1, hit.get().point().z());
        }

        @Test
        @DisplayName("Should hit at various interior points")
        void shouldHitAtInteriorPoints() {
            Ray ray1 = new Ray(Point.of(0.25, -1, 0.25), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray1, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());

            Ray ray2 = new Ray(Point.of(0.5, -1, 0.25), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray2, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());

            Ray ray3 = new Ray(Point.of(0.1, -1, 0.1), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray3, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());
        }

        @Test
        @DisplayName("Should hit at edge midpoints")
        void shouldHitAtEdgeMidpoints() {
            Ray ray1 = new Ray(Point.of(0.5, -1, 0), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray1, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());

            Ray ray2 = new Ray(Point.of(0, -1, 0.5), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray2, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());

            Ray ray3 = new Ray(Point.of(0.5, -1, 0.5), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray3, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());
        }

        @Test
        @DisplayName("Should NOT hit just outside triangle boundaries")
        void shouldNotHitOutsideBoundaries() {
            Ray ray1 = new Ray(Point.of(-0.01, -1, 0), new Vector3d(0, 1, 0));
            assertFalse(triangle.hit(ray1, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent(),
                    "Should not hit at x=-0.01");

            Ray ray2 = new Ray(Point.of(1.01, -1, 0), new Vector3d(0, 1, 0));
            assertFalse(triangle.hit(ray2, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent(),
                    "Should not hit at x=1.01");

            Ray ray3 = new Ray(Point.of(0, -1, -0.01), new Vector3d(0, 1, 0));
            assertFalse(triangle.hit(ray3, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent(),
                    "Should not hit at z=-0.01");

            Ray ray4 = new Ray(Point.of(0, -1, 1.01), new Vector3d(0, 1, 0));
            assertFalse(triangle.hit(ray4, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent(),
                    "Should not hit at z=1.01");

            Ray ray5 = new Ray(Point.of(0.6, -1, 0.6), new Vector3d(0, 1, 0));
            assertFalse(triangle.hit(ray5, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent(),
                    "Should not hit beyond hypotenuse");
        }

        @Test
        @DisplayName("Should not hit when ray misses triangle completely")
        void shouldNotHitWhenRayMisses() {
            Ray ray = new Ray(Point.of(5, -1, 5), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should not hit when ray is parallel to XZ triangle")
        void shouldNotHitWhenRayIsParallel() {
            Ray ray = new Ray(Point.of(0, 0, 0.5), new Vector3d(1, 0, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should not hit when intersection is outside time interval")
        void shouldNotHitWhenOutsideTimeInterval() {
            Ray ray = new Ray(Point.of(0.3, -10, 0.3), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, 5);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should hit when ray starts from below and points up")
        void shouldHitFromBelow() {
            Ray ray = new Ray(Point.of(0.3, 1, 0.3), new Vector3d(0, -1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent());
            assertEquals(0.3, hit.get().point().x());
            assertEquals(0, hit.get().point().y());
            assertEquals(0.3, hit.get().point().z());
        }

        @Test
        @DisplayName("Should return correct hit time")
        void shouldReturnCorrectHitTime() {
            Ray ray = new Ray(Point.of(0.3, -5, 0.3), new Vector3d(0, 1, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = triangle.hit(ray, interval);

            assertTrue(hit.isPresent());
            assertEquals(5.0, hit.get().time());
        }

        @Test
        @DisplayName("Should have correct normal direction")
        void shouldHaveCorrectNormal() {
            Ray ray = new Ray(Point.of(0.3, -1, 0.3), new Vector3d(0, 1, 0));
            Optional<HitRecord> hit = triangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY));

            assertTrue(hit.isPresent());
            Vector3d normal = hit.get().normal();

            assertEquals(0, normal.x);
            assertEquals(1, Math.abs(normal.y));
            assertEquals(0, normal.z);
        }

        @Test
        @DisplayName("Should correctly handle barycentric boundary at u=0")
        void shouldHandleBarycentricBoundaryU0() {
            Ray ray = new Ray(Point.of(0, -1, 0.5), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());
        }

        @Test
        @DisplayName("Should correctly handle barycentric boundary at v=0")
        void shouldHandleBarycentricBoundaryV0() {
            Ray ray = new Ray(Point.of(0.5, -1, 0), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());
        }

        @Test
        @DisplayName("Should correctly handle barycentric boundary at u+v=1")
        void shouldHandleBarycentricBoundarySum1() {
            Ray ray = new Ray(Point.of(0.5, -1, 0.5), new Vector3d(0, 1, 0));
            assertTrue(triangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY)).isPresent());
        }
    }

    @Nested
    @DisplayName("Different Plane Orientations")
    class DifferentOrientationTests {

        @Test
        @DisplayName("Should hit triangle in XY plane")
        void shouldHitTriangleInXYPlane() {
            Triangle xyTriangle = new Triangle(redColor,
                    Point.of(0, 0, 0),
                    Point.of(1, 0, 0),
                    Point.of(0, -1, 0));

            Ray ray = new Ray(Point.of(0.25, -0.25, -1), new Vector3d(0, 0, 1));
            Optional<HitRecord> hit = xyTriangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY));

            assertTrue(hit.isPresent());
            assertEquals(0.25, hit.get().point().x());
            assertEquals(-0.25, hit.get().point().y());
            assertEquals(0, hit.get().point().z());
        }

        @Test
        @DisplayName("Should hit triangle in YZ plane")
        void shouldHitTriangleInYZPlane() {
            Triangle yzTriangle = new Triangle(redColor,
                    Point.of(0, 0, 0),
                    Point.of(0, -1, 0),
                    Point.of(0, 0, 1));

            Ray ray = new Ray(Point.of(-1, -0.25, 0.25), new Vector3d(1, 0, 0));
            Optional<HitRecord> hit = yzTriangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY));

            assertTrue(hit.isPresent());
            assertEquals(0, hit.get().point().x());
            assertEquals(-0.25, hit.get().point().y());
            assertEquals(0.25, hit.get().point().z());
        }

        @Test
        @DisplayName("Should hit arbitrarily oriented triangle")
        void shouldHitArbitrarilyOrientedTriangle() {
            Triangle arbitraryTriangle = new Triangle(redColor,
                    Point.of(0, 0, 0),
                    Point.of(1, -1, 0),
                    Point.of(0, -1, 1));

            Point centroid = arbitraryTriangle.getCentroid();

            Vector3d u = Point.of(1, -1, 0).value().sub(Point.of(0, 0, 0).value(), new Vector3d());
            Vector3d v = Point.of(0, -1, 1).value().sub(Point.of(0, 0, 0).value(), new Vector3d());
            Vector3d normal = u.cross(v, new Vector3d()).normalize();

            Point rayOrigin = Point.of(
                    centroid.x() - normal.x,
                    centroid.y() - normal.y,
                    centroid.z() - normal.z
            );
            Ray ray = new Ray(rayOrigin, normal);

            Optional<HitRecord> hit = arbitraryTriangle.hit(ray, Interval.of(0, Double.POSITIVE_INFINITY));
            assertTrue(hit.isPresent());
        }
    }

    @Nested
    @DisplayName("Transformation Tests")
    class TransformationTests {

        @Test
        @DisplayName("Should transform all vertices with transformation matrix")
        void shouldTransformVertices() {
            Matrix translationMatrix = Matrix.translation(1, 0, 0);

            Triangle transformed = triangle.transform(
                    Optional.of(translationMatrix),
                    Optional.empty()
            );

            assertEquals(1.0, transformed.p1().x());
            assertEquals(2.0, transformed.p2().x());
            assertEquals(1.0, transformed.p3().x());
        }

        @Test
        @DisplayName("Should not transform when no matrix provided")
        void shouldNotTransformWithoutMatrix() {
            Triangle transformed = triangle.transform(
                    Optional.empty(),
                    Optional.empty()
            );

            assertEquals(p1, transformed.p1());
            assertEquals(p2, transformed.p2());
            assertEquals(p3, transformed.p3());
        }

    }

}