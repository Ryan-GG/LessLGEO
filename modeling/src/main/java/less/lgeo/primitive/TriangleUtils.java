package less.lgeo.primitive;

import java.util.List;
import java.util.Optional;
import less.lgeo.common.Color;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.common.VertexUtils;

public class TriangleUtils {

  public static Triangle getTriangle( Color color, Vertex p1, Vertex p2, Vertex p3 ) {
    return Triangle.newBuilder()
        .setColor( color )
        .setP1( p1 )
        .setP2( p2 )
        .setP3( p3 )
        .build();
  }

  public static List<Vertex> getVertices( Triangle triangle ) {
    return List.of( triangle.getP1(), triangle.getP2(), triangle.getP3() );
  }

  public static Triangle transformTriangle( Triangle triangle,
      Optional<Matrix> transformationMatrix ) {
    Vertex p1 = triangle.getP1();
    Vertex p2 = triangle.getP2();
    Vertex p3 = triangle.getP3();
    return getTriangle(
        triangle.getColor(),
        transformationMatrix.map( value -> VertexUtils.transform( p1, value ) ).orElse( p1 ),
        transformationMatrix.map( value -> VertexUtils.transform( p2, value ) ).orElse( p2 ),
        transformationMatrix.map( value -> VertexUtils.transform( p3, value ) ).orElse( p3 )
    );
  }
}
