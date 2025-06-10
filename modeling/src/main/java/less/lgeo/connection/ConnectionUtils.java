package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_X_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.BRICK_Z_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.VertexUtils.getPoint;
import static less.lgeo.common.VertexUtils.transform;

import java.util.List;
import java.util.stream.Stream;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;

public class ConnectionUtils {

  /**
   * @return List of rendered connection points as {@link Vertex}
   */
  public static List<Vertex> getConnectionPoints( Connection connection ) {
    return connection.getPartConnectionList().stream()
        .flatMap( partConnection -> switch ( partConnection.getGroupId() ) {
          case GROUP_ZERO -> Stream.empty();
          case GROUP_ONE -> Stream.empty();
          case GROUP_STUD -> getGroupStudVertices( partConnection );
          case GROUP_FOUR -> Stream.empty();
          case GROUP_SIX -> Stream.empty();
          default -> Stream.empty();
        } ).toList();
  }

  public static Vertex getPartConnectionOrigin( PartConnection partConnection ) {
    Matrix matrix = partConnection.getMatrix();
    return getPoint( matrix.getX(), matrix.getY(), matrix.getZ() );
  }

  public static Stream<Vertex> getGroupStudVertices( PartConnection partConnection ) {
    Matrix matrix = partConnection.getMatrix();
    GroupStud studGeometry = partConnection.getGroupStud();
    double x = matrix.getX();
    double y = matrix.getY();
    double z = matrix.getZ();

    Vertex topLeft = getPoint( x, y, z );

    Vertex topRight = getPoint(
        x + ( ( double ) ( BRICK_X_TO_LDU / 2 ) * studGeometry.getXWidthHalfStud() ), y, z );

    Vertex center = getPoint(
        x + ( ( double ) BRICK_X_TO_LDU / studGeometry.getXWidthHalfStud() ),
        y - STUD_HEIGHT,
        z - ( ( double ) BRICK_Z_TO_LDU / studGeometry.getZWidthHalfStud() ) );

    Vertex bottomLeft = getPoint(
        x,
        y,
        z - ( ( double ) ( BRICK_Z_TO_LDU / 2 ) * studGeometry.getZWidthHalfStud() ) );

    Vertex bottomRight = getPoint(
        x + ( ( double ) ( BRICK_X_TO_LDU / 2 ) * studGeometry.getXWidthHalfStud() ),
        y,
        z - ( ( double ) ( BRICK_Z_TO_LDU / 2 ) * studGeometry.getZWidthHalfStud() ) );

    return Stream.of( topLeft, topRight, center, bottomLeft, bottomRight );
  }

  /**
   * Transforms by the 'dat' transformation matrix, as each connection matrix is the identity
   *
   * @param connection           Model Connection
   * @param transformationMatrix 'dat' / 'piece' matrix
   * @return Transformed connected by 'piece' transformation matrix
   */
  public static Connection transformConnection( Connection connection,
      Matrix transformationMatrix ) {
    Connection.Builder builder = connection.toBuilder();

    List<PartConnection> transformedPartConnections = connection.getPartConnectionList().stream()
        .map( partConnection -> {

          Vertex origin = getPartConnectionOrigin( partConnection );
          Vertex newOrigin = transform( origin, transformationMatrix );

          Matrix oldMatrix = partConnection.getMatrix();
          Matrix newMatrix = oldMatrix.toBuilder()
              .setX( newOrigin.getX() )
              .setY( newOrigin.getY() )
              .setZ( newOrigin.getZ() )
              .build();

          return partConnection.toBuilder()
              .setMatrix( newMatrix )
              .build();
        } )
        .toList();

    return builder.clearPartConnection()
        .addAllPartConnection( transformedPartConnections )
        .build();
  }

}
