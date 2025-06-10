package less.lgeo.connection;

import static less.lgeo.LDrawUnitsUtil.BRICK_X_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.BRICK_Z_TO_LDU;
import static less.lgeo.LDrawUnitsUtil.STUD_HEIGHT;
import static less.lgeo.common.VertexUtils.getPoint;

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

  public static Stream<Vertex> getGroupStudVertices( PartConnection partConnection ) {
    Matrix matrix = partConnection.getMatrix();
    GroupStud studGeometry = partConnection.getGroupStud();
    double x = matrix.getX();
    double y = matrix.getY();
    double z = matrix.getZ();

    // x, y, z
    Vertex topLeft = getPoint( x, y, z );

    // x + (half stud * x_width), y , z
    Vertex topRight = getPoint(
        x + ( ( double ) BRICK_X_TO_LDU / studGeometry.getXWidthHalfStud() ), y, z );

    // x + (half stud * x_width) / 2, y - STUD_HEIGHT , z - (half stud * z_width) / 2
    Vertex center = getPoint(
        x + ( ( double ) ( BRICK_X_TO_LDU / studGeometry.getXWidthHalfStud() ) / 2 ),
        y - STUD_HEIGHT,
        z - ( ( double ) ( BRICK_Z_TO_LDU / studGeometry.getZWidthHalfStud() ) / 2 ) );

    // x, y , z - (half stud * z_width)
    Vertex bottomLeft = getPoint(
        x,
        y,
        z - ( ( double ) BRICK_Z_TO_LDU / studGeometry.getZWidthHalfStud() ) );

    // x + (half stud * z_width), y , z - (half stud * z_width)
    Vertex bottomRight = getPoint(
        x + ( ( double ) BRICK_X_TO_LDU / studGeometry.getXWidthHalfStud() ),
        y,
        z - ( ( double ) BRICK_Z_TO_LDU / studGeometry.getZWidthHalfStud() ) );
    return Stream.of( topLeft, topRight, center, bottomLeft, bottomRight );
  }

}
