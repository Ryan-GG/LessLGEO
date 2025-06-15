package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.HALF_BRICK_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.VertexUtils.getPoint;
import static less.lgeo.common.VertexUtils.transform;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;

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

  public static Vertex getPartConnectionOrigin( PartConnection partConnection ) {
    Matrix matrix = partConnection.getMatrix();
    return getPoint( matrix.getX(), matrix.getY(), matrix.getZ() );
  }

  private static Stream<Vertex> getGroupStudVertices( PartConnection partConnection ) {
    GroupStud studGeometry = partConnection.getGroupStud();

    Vertex origin = getPartConnectionOrigin( partConnection );

    double x = origin.getX();
    double y = origin.getY();
    double z = origin.getZ();

    Stream<Vertex> connectionVertices = Stream.empty();

    for ( int row = 0; row < studGeometry.getZWidthHalfStud() / 2; row++ ) {
      for ( int col = 0; col < studGeometry.getXWidthHalfStud() / 2; col++ ) {
        double xTranslatedOrigin = x + ( BRICK_TO_LDU * col );
        // Origin exists in top left thus, negative z value
        double zTranslatedOrigin = z - ( BRICK_TO_LDU * row );

        Vertex topLeft = getPoint( xTranslatedOrigin, y, zTranslatedOrigin );

        Vertex topRight = getPoint(
            xTranslatedOrigin + BRICK_TO_LDU,
            y, zTranslatedOrigin );

        Vertex center = getPoint(
            xTranslatedOrigin + HALF_BRICK_TO_LDU,
            // LDraw uses a right-handed co-ordinate system where -Y is "up".
            y - STUD_HEIGHT,
            zTranslatedOrigin - HALF_BRICK_TO_LDU );

        Vertex bottomLeft = getPoint(
            xTranslatedOrigin,
            y,
            zTranslatedOrigin - BRICK_TO_LDU );

        Vertex bottomRight = getPoint(
            xTranslatedOrigin + BRICK_TO_LDU,
            y,
            zTranslatedOrigin - BRICK_TO_LDU );

        Stream<Vertex> studPoints = Stream.of( topLeft, topRight, center, bottomLeft, bottomRight );

        connectionVertices = Stream.concat( connectionVertices, studPoints );
      }
    }
    return connectionVertices.map( vertex -> transform( vertex, partConnection.getMatrix() ) );
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

          Matrix connectionMatrix = partConnection.getMatrix();
          Vertex topLeft = getPoint( connectionMatrix.getX() + transformationMatrix.getX(),
              connectionMatrix.getY() + transformationMatrix.getY(),
              connectionMatrix.getZ() + transformationMatrix.getZ() );

          Matrix newMatrix = transformationMatrix.toBuilder()
              .setX( topLeft.getX() )
              .setY( topLeft.getY() )
              .setZ( topLeft.getZ() )
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
