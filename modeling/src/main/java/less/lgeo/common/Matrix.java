package less.lgeo.common;

import lombok.Builder;
import org.ejml.data.DMatrix4x4;

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

    /**
     * @formatter:off
     * / a b c x \
     * | d e f y |
     * | g h i z |
     * \ 0 0 0 1 /
     * @formatter:on
     */
    public static DMatrix4x4 matrixToDMatrix(Matrix matrix) {
        return new DMatrix4x4(
                matrix.a(), matrix.b(), matrix.c(), matrix.x(),
                matrix.d(), matrix.e(), matrix.f(), matrix.y(),
                matrix.g(), matrix.h(), matrix.i(), matrix.z(),
                0.0, 0.0, 0.0, matrix.scale());
    }

    public static Matrix dMatrixToMatrix(DMatrix4x4 matrix) {
        return new Matrix(
                matrix.a11, matrix.a12, matrix.a13, matrix.a14,
                matrix.a21, matrix.a22, matrix.a23, matrix.a24,
                matrix.a31, matrix.a32, matrix.a33, matrix.a34,
                matrix.a44);
    }

    /**
     * @formatter:off
     * / a b c x \
     * | d e f y |
     * | g h i z |
     * \ 0 0 0 1 /
     * @formatter:on
     */
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
