package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.getColor;

import java.util.List;
import java.util.Optional;
import less.lgeo.common.Color;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

public class OptionalLineUtils {


  public static OptionalLine toOptionalLine( Color color, Vertex p1, Vertex p2, Vertex p3,
      Vertex p4 ) {
    return OptionalLine.newBuilder()
        .setColor( color )
        .setP1( p1 )
        .setP2( p2 )
        .setP3( p3 )
        .setP4( p4 )
        .build();
  }

  /**
   * @return List of rendered line {@link Vertex}, disregard control points.
   */
  public static List<Vertex> getVertices( OptionalLine optionalLine ) {
    return List.of(
        optionalLine.getP1(),
        optionalLine.getP2() );
  }

  public static OptionalLine transformOptionalLine( OptionalLine optionalLine,
      Optional<Matrix> transformationMatrix,
      Optional<Color> inheritedColor ) {
    Vertex p1 = optionalLine.getP1();
    Vertex p2 = optionalLine.getP2();
    Vertex p3 = optionalLine.getP1();
    Vertex p4 = optionalLine.getP2();
    return toOptionalLine(
        getColor( inheritedColor, optionalLine.getColor() ),
        transformationMatrix.map( value -> VertexUtils.transform( p1, value ) ).orElse( p1 ),
        transformationMatrix.map( value -> VertexUtils.transform( p2, value ) ).orElse( p2 ),
        transformationMatrix.map( value -> VertexUtils.transform( p3, value ) ).orElse( p3 ),
        transformationMatrix.map( value -> VertexUtils.transform( p4, value ) ).orElse( p4 )
    );
  }
}
