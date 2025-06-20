package less.lgeo.primitive;

import java.util.List;
import java.util.Optional;
import less.lgeo.common.Color;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

public class LineUtils {

  public static Line getLine( Color color, Vertex p1, Vertex p2 ) {
    return Line.newBuilder()
        .setColor( color )
        .setP1( p1 )
        .setP2( p2 )
        .build();
  }

  public static List<Vertex> getVertices( Line line ) {
    return List.of( line.getP1(), line.getP2() );
  }

  public static Line transformLine( Line line, Optional<Matrix> transformationMatrix ) {
    Vertex p1 = line.getP1();
    Vertex p2 = line.getP2();
    return getLine(
        line.getColor(),
        transformationMatrix.map( value -> VertexUtils.transform( p1, value ) ).orElse( p1 ),
        transformationMatrix.map( value -> VertexUtils.transform( p2, value ) ).orElse( p2 )
    );
  }
}
