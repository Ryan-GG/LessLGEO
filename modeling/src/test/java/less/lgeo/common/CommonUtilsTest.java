package less.lgeo.common;

import org.ejml.data.DMatrix4x4;
import org.joml.Vector3d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonUtilsTest {

    @Test
    void testGpbToDMatrixAndBack() {
        Matrix m = Matrix.builder()
                .a(1).d(2).g(3).x(4)
                .b(5).e(6).h(7).y(8)
                .c(9).f(10).i(11).z(12)
                .scale(1.0)
                .build();
        DMatrix4x4 dMatrixFromMatrix = Matrix.matrixToDMatrix(m);
        Matrix matrixFromDMatrix = Matrix.dMatrixToMatrix(dMatrixFromMatrix);
        assertEquals(m.getA(), matrixFromDMatrix.getA());
        assertEquals(m.getB(), matrixFromDMatrix.getB());
        assertEquals(m.getC(), matrixFromDMatrix.getC());
        assertEquals(m.getD(), matrixFromDMatrix.getD());
        assertEquals(m.getE(), matrixFromDMatrix.getE());
        assertEquals(m.getF(), matrixFromDMatrix.getF());
        assertEquals(m.getG(), matrixFromDMatrix.getG());
        assertEquals(m.getH(), matrixFromDMatrix.getH());
        assertEquals(m.getI(), matrixFromDMatrix.getI());
        assertEquals(m.getX(), matrixFromDMatrix.getX());
        assertEquals(m.getY(), matrixFromDMatrix.getY());
        assertEquals(m.getZ(), matrixFromDMatrix.getZ());
        assertEquals(m.getScale(), matrixFromDMatrix.getScale());
    }

    @Test
    void testVector3TransformIdentity() {
        Matrix identity = Matrix.builder()
                .a(1).d(0).g(0).x(0)
                .b(0).e(1).h(0).y(0)
                .c(0).f(0).i(1).z(0)
                .scale(1.0)
                .build();
        Vector3d vertex = new Vector3d(1, 2, 3);
        Vector3d result = Vector3Utils.transform(vertex, identity);
        assertEquals(1, result.x);
        assertEquals(2, result.y);
        assertEquals(3, result.z);
    }

    @Test
    void testVector3TransformTranslation() {
        Matrix translation = Matrix.builder()
                .a(1).d(0).g(0).x(5)
                .b(0).e(1).h(0).y(-3)
                .c(0).f(0).i(1).z(2)
                .scale(1.0)
                .build();
        Vector3d transform = new Vector3d(1, 2, 3);
        Vector3d result = Vector3Utils.transform(transform, translation);
        assertEquals(6, result.x);
        assertEquals(-1, result.y);
        assertEquals(5, result.z);
    }

    @Test
    void testVector3TransformRotationZ45() {
        double sqrt2over2 = Math.sqrt(2) / 2.0;
        Matrix rotZ45 = Matrix.builder()
                .a(sqrt2over2).d(sqrt2over2).g(0).x(0)
                .b(-sqrt2over2).e(sqrt2over2).h(0).y(0)
                .c(0).f(0).i(1).z(0)
                .scale(1.0)
                .build();
        Vector3d transform = new Vector3d(1, 0, 0);
        Vector3d result = Vector3Utils.transform(transform, rotZ45);
        assertEquals(sqrt2over2, result.x);
        assertEquals(sqrt2over2, result.y;
        assertEquals(0, result.z);
    }
}
