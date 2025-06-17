package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.CommonUtils.dMatrixToGpb;
import static less.lgeo.common.CommonUtils.gpbMatrixToString;
import static less.lgeo.common.CommonUtils.gpbToDMatrix;
import static less.lgeo.common.VertexUtils.getPoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;
import org.ejml.data.DMatrix4x4;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.fixed.CommonOps_DDF4;
import org.ejml.dense.row.CommonOps_DDRM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionUtils {

  private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

  /**
   * @return Set of rendered connection points as {@link Vertex}
   */
  public static Set<Vertex> getConnectionPoints(Connection connection) {
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

  public static Vertex getPartConnectionOrigin(PartConnection partConnection) {
    Matrix matrix = partConnection.getMatrix();
    return getPoint(matrix.getX(), matrix.getY(), matrix.getZ());
  }

  private static Stream<Vertex> getGroupStudVertices(PartConnection partConnection) {
    GroupStud studGeometry = partConnection.getGroupStud();

    DMatrix4x4 fullMatrix = gpbToDMatrix(partConnection.getMatrix());
    DMatrixRMaj transform = new DMatrixRMaj(4, 4);
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        transform.set(row, col, fullMatrix.get(row, col));
      }
    }

    int zStuds = studGeometry.getZWidthHalfStud() / 2;  // 4 -> 2 studs
    int xStuds = studGeometry.getXWidthHalfStud() / 2;  // 8 -> 4 studs

    Stream<Vertex> connectionVertices = Stream.empty();

    for (int z = 0; z < zStuds; z++) {
      for (int x = 0; x < xStuds; x++) {
        // center of each stud cell
        double xOffset = (-xStuds / 2.0 + x + 0.5) * BRICK_TO_LDU;
        double zOffset = (zStuds / 2.0 - z - 0.5) * BRICK_TO_LDU; // top to bottom

        // homogeneous input vector
        DMatrixRMaj pointVector = new DMatrixRMaj(4, 1);
        pointVector.set(0, 0, xOffset);
        pointVector.set(1, 0, -STUD_HEIGHT);
        pointVector.set(2, 0, zOffset);
        pointVector.set(3, 0, 1.0);

        DMatrixRMaj resultVector = new DMatrixRMaj(4, 1);
        CommonOps_DDRM.mult(transform, pointVector, resultVector);

        double worldX = resultVector.get(0, 0);
        double worldY = resultVector.get(1, 0);
        double worldZ = resultVector.get(2, 0);

        Vertex v = getPoint(worldX, worldY, worldZ);
        connectionVertices = Stream.concat(connectionVertices, Stream.of(v));
      }
    }

    return connectionVertices;
  }

  /**
   * Transforms by the 'dat' transformation matrix, as each connection matrix is the identity
   *
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
          CommonOps_DDF4.mult(gpbToDMatrix(transformationMatrix),
              gpbToDMatrix(partConnection.getMatrix()),
              result);
          Matrix resulted = dMatrixToGpb(result);

          logger.info(" part connection transformation matrix {}", gpbMatrixToString(resulted));

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
