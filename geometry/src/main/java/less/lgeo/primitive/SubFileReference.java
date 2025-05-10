package less.lgeo.primitive;

import java.nio.file.Path;
import less.lgeo.Matrix;

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
  private final Path fileReference;

  public SubFileReference(Color color, double x, double y, double z, double a, double b, double c,
      double d, double e,
      double f, double g, double h, double i, Path fileReference) {
    this.color = color;
    this.matrix = new Matrix(x, y, z, a, b, c, d, e, f, g, h, i);
    this.fileReference = fileReference;
  }

  public SubFileReference(Color color, Matrix matrix, Path fileReference) {
    this.color = color;
    this.matrix = matrix;
    this.fileReference = fileReference;
  }

  public Color getColor() {
    return this.color;
  }

  public Matrix getMatrix() {
    return this.matrix;
  }


  /**
   * sub-files can be located in the LDRAW\PARTS sub-directory, the LDRAW\P sub-directory, the
   * LDRAW\MODELS sub-directory, the current file's directory, a path relative to one of these
   * directories, or a full path may be specified. Sub-parts are typically stored in the
   * LDRAW\PARTS\S sub-directory and so are referenced as s\subpart.dat, while hi-res primitives are
   * stored in the LDRAW\P\48 sub-directory and so referenced as 48\hires.dat
   * <p>
   * While there is no specified limit on how deep sub-files may be nested, there are probably
   * practical limitations imposed by individual software programs.
   * <p>
   * There are many on-line references about transformation matrices, one such reference is 3D
   * Transformations – Part 1 Matrices
   *
   * @return
   */
  public Path getFileReference() {
    return this.fileReference;
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
        this.fileReference.toString()
    );
  }
}
