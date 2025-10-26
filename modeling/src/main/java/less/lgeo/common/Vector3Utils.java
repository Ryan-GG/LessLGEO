package less.lgeo.common;

import org.joml.Vector3d;

public class Vector3Utils {

    /**
     * u' = a*u + b*v + c*w + x
     * v' = d*u + e*v + f*w + y
     * w' = g*u + h*v + i*w + z
     *
     * @param matrix Matrix that holds transformation
     * @return The resulting {@link Vector3d} from the previous position with the
     * transformation
     * applied.
     */
    public static Vector3d transform(Vector3d vector, Matrix matrix) {
        double x = vector.x;
        double y = vector.y;
        double z = vector.z;

        double newX = matrix.getA() * x + matrix.getB() * y
                + matrix.getC() * z + matrix.getX();
        double newY = matrix.getD() * x + matrix.getE() * y
                + matrix.getF() * z + matrix.getY();
        double newZ = matrix.getG() * x + matrix.getH() * y
                + matrix.getI() * z + matrix.getZ();

        return new Vector3d(newX, newY, newZ);
    }
}
