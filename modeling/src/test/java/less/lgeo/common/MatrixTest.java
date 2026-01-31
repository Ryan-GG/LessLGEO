package less.lgeo.common;

import org.joml.Matrix4d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatrixTest {

    @Test
    void dMatrixAndBack() {
        Matrix m = Matrix.builder()
                .a(1).d(2).g(3).x(4)
                .b(5).e(6).h(7).y(8)
                .c(9).f(10).i(11).z(12)
                .scale(1.0)
                .build();
        Matrix4d dMatrixFromMatrix = Matrix.toMatrix4d(m);
        Matrix matrixFromDMatrix = Matrix.fromMatrix4d(dMatrixFromMatrix);
        assertEquals(m.a(), matrixFromDMatrix.a());
        assertEquals(m.b(), matrixFromDMatrix.b());
        assertEquals(m.c(), matrixFromDMatrix.c());
        assertEquals(m.d(), matrixFromDMatrix.d());
        assertEquals(m.e(), matrixFromDMatrix.e());
        assertEquals(m.f(), matrixFromDMatrix.f());
        assertEquals(m.g(), matrixFromDMatrix.g());
        assertEquals(m.h(), matrixFromDMatrix.h());
        assertEquals(m.i(), matrixFromDMatrix.i());
        assertEquals(m.x(), matrixFromDMatrix.x());
        assertEquals(m.y(), matrixFromDMatrix.y());
        assertEquals(m.z(), matrixFromDMatrix.z());
        assertEquals(m.scale(), matrixFromDMatrix.scale());
    }

}
