package less.lgeo.common;

import lombok.Builder;
import org.joml.Matrix4d;

/**
 * / a b c x \
 * | d e f y |
 * | g h i z |
 * \ 0 0 0 scale /
 */
@Builder
public record Matrix(
        double a, double b, double c, double x,
        double d, double e, double f, double y,
        double g, double h, double i, double z,
        double scale) {

    public static final Matrix IDENTITY_MATRIX = Matrix.builder()
            .a(1)
            .e(1)
            .i(1)
            .scale(1)
            .build();

    public static Matrix4d toMatrix4d(Matrix matrix) {
        return new Matrix4d(
                matrix.a(), matrix.b(), matrix.c(), matrix.x(),
                matrix.d(), matrix.e(), matrix.f(), matrix.y(),
                matrix.g(), matrix.h(), matrix.i(), matrix.z(),
                0.0, 0.0, 0.0, matrix.scale());
    }

    public static Matrix fromMatrix4d(Matrix4d matrix) {
        return new Matrix(
                matrix.m00(), matrix.m01(), matrix.m02(), matrix.m03(),
                matrix.m10(), matrix.m11(), matrix.m12(), matrix.m13(),
                matrix.m20(), matrix.m21(), matrix.m22(), matrix.m23(),
                matrix.m33());
    }

    @Override
    public String toString() {
        return String.format(
                """
                        \n
                        / %.4f %.4f %.4f %.4f\\
                        | %.4f %.4f %.4f %.4f |
                        | %.4f %.4f %.4f %.4f |
                        \\ 0    0    0   %.4f /
                        """,
                a(), b(), c(), x(),
                d(), e(), f(), y(),
                g(), h(), i(), z(), scale());
    }
}
