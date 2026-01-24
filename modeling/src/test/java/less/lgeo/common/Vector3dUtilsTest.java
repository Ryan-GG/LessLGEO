package less.lgeo.common;

import less.lgeo.primitive.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector3dUtilsTest {

    @Test
    void testVector3dTransformIdentity() {
        Matrix identity = Matrix.builder()
                .a(1).d(0).g(0).x(0)
                .b(0).e(1).h(0).y(0)
                .c(0).f(0).i(1).z(0)
                .scale(1.0)
                .build();
        Point vertex = new Point(1, 2, 3);
        Point result = vertex.transform(identity);
        assertEquals(1, result.x());
        assertEquals(2, result.y());
        assertEquals(3, result.z());
    }

    @Test
    void testVector3dTransformTranslation() {
        Matrix translation = Matrix.builder()
                .a(1).d(0).g(0).x(5)
                .b(0).e(1).h(0).y(-3)
                .c(0).f(0).i(1).z(2)
                .scale(1.0)
                .build();
        Point transform = new Point(1, 2, 3);
        Point result = transform.transform(translation);
        assertEquals(6, result.x());
        assertEquals(-1, result.y());
        assertEquals(5, result.z());
    }

    @Test
    void testVector3dTransformRotationZ45() {
        double sqrt2over2 = Math.sqrt(2) / 2.0;
        Matrix rotZ45 = Matrix.builder()
                .a(sqrt2over2).d(sqrt2over2).g(0).x(0)
                .b(-sqrt2over2).e(sqrt2over2).h(0).y(0)
                .c(0).f(0).i(1).z(0)
                .scale(1.0)
                .build();
        Point transform = new Point(1, 0, 0);
        Point result = transform.transform(rotZ45);
        assertEquals(sqrt2over2, result.x());
        assertEquals(sqrt2over2, result.y());
        assertEquals(0, result.z());
    }
}
