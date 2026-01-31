package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.Interval;
import less.lgeo.common.Matrix;
import less.lgeo.common.Ray;
import less.lgeo.hittable.HitRecord;
import org.joml.Vector3d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class QuadrilateralTest {

    private Color redColor;
    private Point p1, p2, p3, p4;
    private Quadrilateral quadrilateral;

    @BeforeEach
    void setUp() {
        redColor = Color.of(255, 0, 0);
        p1 = Point.of(-0.5, 0, 0);
        p2 = Point.of(0.5, 0, 0);
        p3 = Point.of(0.5, -0.5, 0);
        p4 = Point.of(-0.5, -0.5, 0);
        quadrilateral = new Quadrilateral(redColor, p1, p2, p3, p4);
    }


    @Nested
    @DisplayName("Ray Intersection Tests")
    class RayIntersectionTests {

        @Test
        @DisplayName("Should hit quadrilateral when ray intersects center")
        void shouldHitQuadrilateralCenter() {
            Ray ray = new Ray(Point.of(0, 0, -1), new Vector3d(0, 0, 1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertTrue(hit.isPresent());
            HitRecord record = hit.get();
            assertEquals(0, record.point().x());
            assertEquals(0, record.point().y());
            assertEquals(0, record.point().z());
        }

        @Test
        @DisplayName("Should hit quadrilateral when ray intersects top-right corner")
        void shouldHitQuadrilateralCorner() {
            Ray ray = new Ray(Point.of(0.5, -0.5, -1), new Vector3d(0, 0, 1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertTrue(hit.isPresent());
            assertEquals(0.5, hit.get().point().x());
            assertEquals(-0.5, hit.get().point().y());
            assertEquals(0, hit.get().point().z());
        }

        @Test
        @DisplayName("Should not hit when ray misses quadrilateral")
        void shouldNotHitWhenRayMisses() {
            Ray ray = new Ray(Point.of(2, 2, 1), new Vector3d(0, 0, -1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should not hit when ray is parallel to XY plane")
        void shouldNotHitWhenRayIsParallel() {
            Ray ray = new Ray(Point.of(0.5, -0.5, -1), new Vector3d(1, 0, 0));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should not hit when intersection is outside time interval")
        void shouldNotHitWhenOutsideTimeInterval() {
            Ray ray = new Ray(Point.of(0.5, -0.5, -10), new Vector3d(0, 0, 1));
            // Interval that ends before the intersection
            Interval interval = Interval.of(0, 5);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertFalse(hit.isPresent());
        }

        @Test
        @DisplayName("Should hit when ray starts from below and points up")
        void shouldHitFromBelow() {
            Ray ray = new Ray(Point.of(0, 0.5, -1), new Vector3d(0, -0.5, 1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertTrue(hit.isPresent());
            assertEquals(0, hit.get().point().x());
            assertEquals(0, hit.get().point().y());
            assertEquals(0, hit.get().point().z());
        }

        @Test
        @DisplayName("Should return correct hit time")
        void shouldReturnCorrectHitTime() {
            Ray ray = new Ray(Point.of(0, 0, -5), new Vector3d(0, 0, 1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertTrue(hit.isPresent());
            assertEquals(5.0, hit.get().time());
        }

        @Test
        @DisplayName("Should not hit at edge boundary (just outside)")
        void shouldNotHitJustOutsideBoundary() {
            Ray ray = new Ray(Point.of(0.51, -0.5, -1), new Vector3d(0, 0, 1));
            Interval interval = Interval.of(0, Double.POSITIVE_INFINITY);

            Optional<HitRecord> hit = quadrilateral.hit(ray, interval);

            assertFalse(hit.isPresent());
        }
    }

    @Nested
    @DisplayName("Transformation Tests")
    class TransformationTests {

        @Test
        @DisplayName("Should transform all vertices with transformation matrix")
        void shouldTransformVertices() {
            Matrix translationMatrix = Matrix.translation(0.5, 0, 0);

            Quadrilateral transformed = quadrilateral.transform(
                    Optional.of(translationMatrix),
                    Optional.empty()
            );

            assertEquals(0, transformed.p1().x());
            assertEquals(1.0, transformed.p2().x());
            assertEquals(1.0, transformed.p3().x());
            assertEquals(0.0, transformed.p4().x());
        }

        @Test
        @DisplayName("Should not transform when no matrix provided")
        void shouldNotTransformWithoutMatrix() {
            Quadrilateral transformed = quadrilateral.transform(
                    Optional.empty(),
                    Optional.empty()
            );

            assertEquals(p1, transformed.p1());
            assertEquals(p2, transformed.p2());
            assertEquals(p3, transformed.p3());
            assertEquals(p4, transformed.p4());
        }
    }

    @Nested
    @DisplayName("Tessellation Tests")
    class TessellationTests {

        @Test
        @DisplayName("Should tessellate with correct triangle vertices")
        void shouldTessellateWithCorrectVertices() {
            List<Triangle> triangles = quadrilateral.tessellate();

            Triangle bottomLeft = triangles.get(0);
            Triangle topRight = triangles.get(1);

            // Bottom-left triangle: p1, p2, p4
            assertEquals(p1, bottomLeft.p1());
            assertEquals(p2, bottomLeft.p2());
            assertEquals(p4, bottomLeft.p3());

            // Top-right triangle: p2, p3, p4
            assertEquals(p2, topRight.p1());
            assertEquals(p3, topRight.p2());
            assertEquals(p4, topRight.p3());
        }

        @Test
        @DisplayName("Should tessellate with same color as quadrilateral")
        void shouldTessellateWithSameColor() {
            List<Triangle> triangles = quadrilateral.tessellate();

            assertEquals(2, triangles.size());
            assertEquals(redColor, triangles.get(0).color());
            assertEquals(redColor, triangles.get(1).color());
        }
    }

}
