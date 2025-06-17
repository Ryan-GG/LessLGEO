package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.HALF_BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.CommonUtils.dMatrixToGpb;
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

public class ConnectionUtils {
  
  /**
   * @return Set of rendered connection points as {@link Vertex}
   */
  public static Set<Vertex> getConnectionPoints( Connection connection ) {
    return connection.getPartConnectionList().stream()
        .flatMap( partConnection -> switch ( partConnection.getGroupId() ) {
          case GROUP_ZERO -> Stream.empty();
          case GROUP_ONE -> Stream.empty();
          case GROUP_STUD -> getGroupStudVertices( partConnection );
          case GROUP_FOUR -> Stream.empty();
          case GROUP_SIX -> Stream.empty();
          default -> Stream.empty();
        } ).collect( Collectors.toSet() );
  }

  /**
   * @param partConnection connection model
   * @return Gets the stud vertices for a part connection. Creates points by the rotation and
   * translation of the connection matrix
   */
  private static Stream<Vertex> getGroupStudVertices( PartConnection partConnection ) {
    GroupStud studGeometry = partConnection.getGroupStud();

    int zStuds = studGeometry.getZWidthHalfStud() / 2;
    int xStuds = studGeometry.getXWidthHalfStud() / 2;

    Stream<Vertex> connectionVertices = Stream.empty();

    for ( int z = 0; z < zStuds; z++ ) {
      for ( int x = 0; x < xStuds; x++ ) {

        double xStudOffset = -xStuds / 2.0;
        double zStudOffset = zStuds / 2.0;

        // This is to translate so that stud centers align
        double xTranslation = x + 0.5;
        double zTranslation = -z - 0.5;

        double xOffset = ( xStudOffset + xTranslation ) * BRICK_TO_LDU;
        double zOffset = ( zStudOffset + zTranslation ) * BRICK_TO_LDU;

        Vertex topLeft = transformConnectionVertex( xOffset - HALF_BRICK_TO_LDU, 0,
            zOffset + HALF_BRICK_TO_LDU,
            partConnection.getMatrix() );
        Vertex topRight = transformConnectionVertex( xOffset + HALF_BRICK_TO_LDU, 0,
            zOffset + HALF_BRICK_TO_LDU,
            partConnection.getMatrix() );
        Vertex center = transformConnectionVertex( xOffset, -STUD_HEIGHT,
            zOffset,
            partConnection.getMatrix() );
        Vertex bottomLeft = transformConnectionVertex( xOffset - HALF_BRICK_TO_LDU, 0,
            zOffset - HALF_BRICK_TO_LDU,
            partConnection.getMatrix() );
        Vertex bottomRight = transformConnectionVertex( xOffset + HALF_BRICK_TO_LDU, 0,
            zOffset - HALF_BRICK_TO_LDU,
            partConnection.getMatrix() );

        connectionVertices = Stream.concat( connectionVertices,
            Stream.of( topLeft, topRight, center, bottomLeft, bottomRight ) );
      }
    }

    return connectionVertices;
  }


  private static Vertex transformConnectionVertex( double xOffset, double yOffset, double zOffset,
      Matrix partconnectionMatrix ) {

    DMatrixRMaj transformVector = new DMatrixRMaj( 4, 1 );
    transformVector.set( 0, 0, xOffset );
    transformVector.set( 1, 0, yOffset );
    transformVector.set( 2, 0, zOffset );
    transformVector.set( 3, 0, partconnectionMatrix.getScale() );

    DMatrixRMaj resultVector = new DMatrixRMaj( 4, 1 );
    CommonOps_DDRM.mult( new DMatrixRMaj( gpbToDMatrix( partconnectionMatrix ) ), transformVector,
        resultVector );

    double x = resultVector.get( 0, 0 );
    double y = resultVector.get( 1, 0 );
    double z = resultVector.get( 2, 0 );

    return getPoint( x, y, z );
  }

  /**
   * @param connection           Model Connection
   * @param transformationMatrix 'dat' / 'piece' matrix
   * @return Transformed connected by 'piece' transformation matrix
   */
  public static Connection transformConnection( Connection connection,
      Matrix transformationMatrix ) {
    Connection.Builder builder = connection.toBuilder();

    List<PartConnection> transformedPartConnections = connection.getPartConnectionList().stream()
        .map( partConnection -> {

          DMatrix4x4 result = new DMatrix4x4();
          CommonOps_DDF4.mult( gpbToDMatrix( transformationMatrix ),
              gpbToDMatrix( partConnection.getMatrix() ),
              result );
          Matrix resulted = dMatrixToGpb( result );

          return partConnection.toBuilder()
              .setMatrix( resulted )
              .build();
        } )
        .toList();

    return builder.clearPartConnection()
        .addAllPartConnection( transformedPartConnections )
        .build();
  }

}
