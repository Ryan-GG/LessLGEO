package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import less.lgeo.common.Matrix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class MatrixEmbeddable {

  private double x;
  private double y;
  private double z;
  private double a;
  private double b;
  private double c;
  private double d;
  private double e;
  private double f;
  private double g;
  private double h;
  private double i;
  private double scale;

  public MatrixEmbeddable( Matrix matrix ) {
    this.x = matrix.getX();
    this.y = matrix.getY();
    this.z = matrix.getZ();
    this.a = matrix.getA();
    this.b = matrix.getB();
    this.c = matrix.getC();
    this.d = matrix.getD();
    this.e = matrix.getE();
    this.f = matrix.getF();
    this.g = matrix.getG();
    this.h = matrix.getH();
    this.i = matrix.getI();
    this.scale = matrix.getScale();
  }
}
