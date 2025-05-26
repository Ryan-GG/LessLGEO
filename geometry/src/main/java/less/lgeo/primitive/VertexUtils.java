package less.lgeo.primitive;

public class VertexUtils {

  public static Vertex getPoint(double x, double y, double z) {
    return Vertex.newBuilder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public static Vertex transform(Vertex oldVertex, Matrix matrix) {
    double newX = matrix.getA() * oldVertex.getX() + matrix.getB() * oldVertex.getY()
        + matrix.getC() * oldVertex.getZ() + matrix.getX();
    double newY = matrix.getD() * oldVertex.getX() + matrix.getE() * oldVertex.getY()
        + matrix.getF() * oldVertex.getZ() + matrix.getY();
    double newZ = matrix.getG() * oldVertex.getX() + matrix.getH() * oldVertex.getY()
        + matrix.getI() * oldVertex.getZ() + matrix.getZ();
    return Vertex.newBuilder()
        .setX(newX)
        .setY(newY)
        .setZ(newZ)
        .build();
  }
}
