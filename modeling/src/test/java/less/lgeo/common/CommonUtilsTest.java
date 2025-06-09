package less.lgeo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.ejml.data.DMatrix4x4;
import org.junit.jupiter.api.Test;

public class CommonUtilsTest {

  @Test
  void testGpbToDMatrixAndBack() {
    Matrix m = Matrix.newBuilder()
        .setA( 1 ).setD( 2 ).setG( 3 ).setX( 4 )
        .setB( 5 ).setE( 6 ).setH( 7 ).setY( 8 )
        .setC( 9 ).setF( 10 ).setI( 11 ).setZ( 12 )
        .setScale( 1.0 )
        .build();
    DMatrix4x4 dMatrixFromGpb = CommonUtils.gpbToDMatrix( m );
    Matrix gpbFromDMatrix = CommonUtils.dMatrixToGpb( dMatrixFromGpb );
    assertEquals( m.getA(), gpbFromDMatrix.getA() );
    assertEquals( m.getB(), gpbFromDMatrix.getB() );
    assertEquals( m.getC(), gpbFromDMatrix.getC() );
    assertEquals( m.getD(), gpbFromDMatrix.getD() );
    assertEquals( m.getE(), gpbFromDMatrix.getE() );
    assertEquals( m.getF(), gpbFromDMatrix.getF() );
    assertEquals( m.getG(), gpbFromDMatrix.getG() );
    assertEquals( m.getH(), gpbFromDMatrix.getH() );
    assertEquals( m.getI(), gpbFromDMatrix.getI() );
    assertEquals( m.getX(), gpbFromDMatrix.getX() );
    assertEquals( m.getY(), gpbFromDMatrix.getY() );
    assertEquals( m.getZ(), gpbFromDMatrix.getZ() );
    assertEquals( m.getScale(), gpbFromDMatrix.getScale() );
  }

  @Test
  void testVertexTransformIdentity() {
    Matrix identity = Matrix.newBuilder()
        .setA( 1 ).setD( 0 ).setG( 0 ).setX( 0 )
        .setB( 0 ).setE( 1 ).setH( 0 ).setY( 0 )
        .setC( 0 ).setF( 0 ).setI( 1 ).setZ( 0 )
        .setScale( 1.0 )
        .build();
    Vertex vertex = Vertex.newBuilder().setX( 1 ).setY( 2 ).setZ( 3 ).build();
    Vertex result = VertexUtils.transform( vertex, identity );
    assertEquals( 1, result.getX() );
    assertEquals( 2, result.getY() );
    assertEquals( 3, result.getZ() );
  }

  @Test
  void testVertexTransformTranslation() {
    Matrix translation = Matrix.newBuilder()
        .setA( 1 ).setD( 0 ).setG( 0 ).setX( 5 )
        .setB( 0 ).setE( 1 ).setH( 0 ).setY( -3 )
        .setC( 0 ).setF( 0 ).setI( 1 ).setZ( 2 )
        .setScale( 1.0 )
        .build();
    Vertex transform = Vertex.newBuilder().setX( 1 ).setY( 2 ).setZ( 3 ).build();
    Vertex result = VertexUtils.transform( transform, translation );
    assertEquals( 6, result.getX() );
    assertEquals( -1, result.getY() );
    assertEquals( 5, result.getZ() );
  }

  @Test
  void testVertexTransformRotationZ45() {
    double sqrt2over2 = Math.sqrt( 2 ) / 2.0;
    Matrix rotZ45 = Matrix.newBuilder()
        .setA( sqrt2over2 ).setD( sqrt2over2 ).setG( 0 ).setX( 0 )
        .setB( -sqrt2over2 ).setE( sqrt2over2 ).setH( 0 ).setY( 0 )
        .setC( 0 ).setF( 0 ).setI( 1 ).setZ( 0 )
        .setScale( 1.0 )
        .build();
    Vertex transform = Vertex.newBuilder().setX( 1 ).setY( 0 ).setZ( 0 ).build();
    Vertex result = VertexUtils.transform( transform, rotZ45 );
    assertEquals( sqrt2over2, result.getX() );
    assertEquals( sqrt2over2, result.getY() );
    assertEquals( 0, result.getZ() );
  }
} 