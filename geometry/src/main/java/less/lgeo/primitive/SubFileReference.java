package less.lgeo.primitive;

import less.lgeo.Matrix;
import less.lgeo.set.Model;

/**
 * <colour> is a number representing the colour of the part. See the Colours section for allowable
 * colour numbers. x y z is the x y z coordinate of the part a b c d e f g h i is a top left 3x3
 * matrix of a standard 4x4 homogeneous transformation {@link Matrix}.
 * <file> is the filename of the sub-file referenced and must be a valid LDraw filename.
 * Any leading and/or trailing whitespace must be ignored. Normal token separation is otherwise
 * disabled for the filename value.
 */
public class SubFileReference {

  private static final LineType type = LineType.SUB_FILE_REF;

  private final Color color;
  private final Matrix matrix;
  private final less.lgeo.set.Model subModel;

  public SubFileReference(Color color, double x, double y, double z, double a, double b, double c,
      double d, double e,
      double f, double g, double h, double i, Model subModel) {
    this.color = color;
    this.matrix = new Matrix(x, y, z, a, b, c, d, e, f, g, h, i);
    this.subModel = subModel;
  }

  public SubFileReference(Color color, Matrix matrix, Model subModel) {
    this.color = color;
    this.matrix = matrix;
    this.subModel = subModel;
  }

  public Color getColor() {
    return this.color;
  }

  public Matrix getMatrix() {
    return this.matrix;
  }

  @Override
  public String toString() {
    return String.format("""
              SubFileReference {
              color=TODO,
              matrix=%s,
              fileRef=%s
            }""",
        //this.color.toString(),
        this.matrix.toString(),
        this.subModel.toString()
    );
  }
}
