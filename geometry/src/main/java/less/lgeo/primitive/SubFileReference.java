package less.lgeo.primitive;

import less.lgeo.Matrix;

public class SubFileReference {
    private static final LineType type = LineType.SUB_FILE_REF;

    private final Color color;
    private final Matrix matrix;
    private final String fileReference;

    public SubFileReference(Color color, double x, double y, double z, double a, double b, double c, double d, double e,
            double f, double g, double h, double i, String fileReference) {
        this.color = color;
        this.matrix = new Matrix(x, y, z, a, b, c, d, e, f, g, h, i);
        this.fileReference = fileReference;
    }

    public SubFileReference(Color color, Matrix matrix, String fileReference) {
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

    public String getFileReference() {
        return this.fileReference;
    }

}
