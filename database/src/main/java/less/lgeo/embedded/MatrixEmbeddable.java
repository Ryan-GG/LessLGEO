package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import less.lgeo.common.Matrix;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MatrixEmbeddable {

    private double a;
    private double b;
    private double c;
    private double x;
    private double d;
    private double e;
    private double f;
    private double y;
    private double g;
    private double h;
    private double i;
    private double z;
    private double scale;

    public MatrixEmbeddable(Matrix matrix) {
        this.a = matrix.a();
        this.b = matrix.b();
        this.c = matrix.c();
        this.x = matrix.x();
        this.d = matrix.d();
        this.e = matrix.e();
        this.f = matrix.f();
        this.y = matrix.y();
        this.g = matrix.g();
        this.h = matrix.h();
        this.i = matrix.i();
        this.z = matrix.z();
        this.scale = matrix.scale();
    }

    public Matrix toDomain() {
        return new Matrix(a, b, c, x, d, e, f, y, g, h, i, z, scale);
    }

}