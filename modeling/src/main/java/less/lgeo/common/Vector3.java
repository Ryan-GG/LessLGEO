package less.lgeo.common;

import lombok.Data;
import org.joml.Vector3d;

@Data
public class Vector3 {

    private final double x;
    private final double y;
    private final double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3d vector3d) {
        this(vector3d.x(), vector3d.y(), vector3d.z());
    }

    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    /**
     * u' = a*u + b*v + c*w + x
     * v' = d*u + e*v + f*w + y
     * w' = g*u + h*v + i*w + z
     *
     * @param matrix Matrix that holds transformation
     * @return The resulting {@link Vector3} from the previous position with the
     * transformation
     * applied.
     */
    public Vector3 transform(Matrix matrix) {
        double newX = matrix.getA() * x + matrix.getB() * y
                + matrix.getC() * z + matrix.getX();
        double newY = matrix.getD() * x + matrix.getE() * y
                + matrix.getF() * z + matrix.getY();
        double newZ = matrix.getG() * x + matrix.getH() * y
                + matrix.getI() * z + matrix.getZ();

        return new Vector3(newX, newY, newZ);
    }
}
