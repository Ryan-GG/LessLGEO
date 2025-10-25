package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.CommonUtils.dMatrixToGpb;
import static less.lgeo.common.CommonUtils.gpbToDMatrix;
import static less.lgeo.common.Vector3Utils.toVector3;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.CommonOps_DDRM;

public class ConnectionUtils {

  /**
   * @return Set of rendered connection points as {@link Vector3}
   */
  public static Set<Vector3> getConnectionPoints(Connection connection) {
    return connection.getPartConnectionList().stream()
        .flatMap(partConnection -> switch (partConnection.getGroupId()) {
          case GROUP_ZERO -> Stream.empty();
          case GROUP_ONE -> Stream.empty();
          case GROUP_STUD -> getGroupStudVertices(partConnection);
          case GROUP_FOUR -> Stream.empty();
          case GROUP_SIX -> Stream.empty();
          default -> Stream.empty();
        }).collect(Collectors.toSet());
  }

  /**
   * @param partConnection connection model
   * @return Gets the stud vertices for a part connection. Creates points by the
   *         rotation and
   *         translation of the connection matrix
   */
  private static Stream<Vector3> getGroupStudVertices(PartConnection partConnection) {
    GroupStud studGeometry = partConnection.getGroupStud();

    int zStuds = studGeometry.getZWidthHalfStud() / 2;
    int xStuds = studGeometry.getXWidthHalfStud() / 2;

    Stream<Vector3> connectionVertices = Stream.empty();

    for (int z = 0; z < zStuds; z++) {
      for (int x = 0; x < xStuds; x++) {

        int index = (2 * z + 1) * (studGeometry.getXWidthHalfStud() + 1) + (2 * x + 1);
        if (!studGeometry.getStudGrid(index)) {
          continue;
        }

        double xStudOffset = -xStuds / 2.0;
        double zStudOffset = zStuds / 2.0;

        // This is to translate so that stud centers align
        double xTranslation = x + 0.5;
        double zTranslation = -z - 0.5;

        double xOffset = (xStudOffset + xTranslation) * BRICK_TO_LDU;
        double zOffset = (zStudOffset + zTranslation) * BRICK_TO_LDU;

        Vector3 center = transformConnectionVector3(xOffset, -STUD_HEIGHT,
            zOffset,
            partConnection.getMatrix());

        connectionVertices = Stream.concat(connectionVertices,
            Stream.of(center));
      }
    }

    return connectionVertices;
  }

  private static Vector3 transformConnectionVector3(double xOffset, double yOffset, double zOffset,
      Matrix partconnectionMatrix) {

    DMatrixRMaj transformVector = new DMatrixRMaj(4, 1);
    transformVector.set(0, 0, xOffset);
    transformVector.set(1, 0, yOffset);
    transformVector.set(2, 0, zOffset);
    transformVector.set(3, 0, partconnectionMatrix.getScale());

    DMatrixRMaj resultVector = new DMatrixRMaj(4, 1);
    CommonOps_DDRM.mult(new DMatrixRMaj(matrixToDMatrix(partconnectionMatrix)), transformVector,
        resultVector);

    double x = resultVector.get(0, 0);
    double y = resultVector.get(1, 0);
    double z = resultVector.get(2, 0);

    return toVector3(x, y, z);
  }

  /**
   * @param connection           Model Connection
   * @param transformationMatrix 'dat' / 'piece' matrix
   * @return Transformed connected by 'piece' transformation matrix
   */
  public static Connection transformConnection(Connection connection,
      Matrix transformationMatrix) {
    Connection.Builder builder = connection.toBuilder();

    List<PartConnection> transformedPartConnections = connection.getPartConnectionList().stream()
        .map(partConnection -> {

          DMatrix4x4 result = new DMatrix4x4();
          CommonOps_DDF4.mult(matrixToDMatrix(transformationMatrix),
              matrixToDMatrix(partConnection.getMatrix()),
              result);
          Matrix resulted = dMatrixToMatrix(result);

          return partConnection.toBuilder()
              .setMatrix(resulted)
              .build();
        })
        .toList();

    return builder.clearPartConnection()
        .addAllPartConnection(transformedPartConnections)
        .build();
  }

}
