package less.lgeo.mesh;

import static javafx.scene.shape.VertexFormat.POINT_TEXCOORD;
import static less.lgeo.connection.ConnectionUtils.getConnectionPoints;
import static less.lgeo.primitive.ModelUtils.getConnections;
import static less.lgeo.primitive.ModelUtils.getLines;
import static less.lgeo.primitive.ModelUtils.getOptionalLines;
import static less.lgeo.primitive.ModelUtils.getQuadrilaterals;
import static less.lgeo.primitive.ModelUtils.getTriangles;
import static less.lgeo.primitive.OptionalLineUtils.getVertices;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.LineUtils;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.QuadrilateralUtils;
import less.lgeo.primitive.TriangleUtils;
import org.fxyz3d.geometry.Point3D;
import org.fxyz3d.shapes.composites.PolyLine3D;
import org.fxyz3d.shapes.primitives.CubeMesh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelMesh {

  private static final Logger logger = LoggerFactory.getLogger( ModelMesh.class );
  private static final Float CONN_SIZE = 1.0f;
  private static final Float LINE_WIDTH = 0.5f;

  private static final Color CONN_COLOR = Color.DEEPPINK;
  private static final Color LINE_COLOR = Color.BLACK;
  private static final Color OPTIONAL_LINE_COLOR = Color.PURPLE;

  private Group mesh;

  public ModelMesh( Model model ) {
    setMesh( model );
  }

  public static Point3D gpbToPoint3D( Vertex point ) {
    return new Point3D( point.getX(), point.getY(), point.getZ() );
  }

  public Group getMesh() {
    return this.mesh;
  }

  private void setMesh( Model model ) {
    List<Node> children = new ArrayList<>();
    children.addAll( drawLines( model ) );
    children.addAll( drawQuadrilaterals( model ) );
    children.addAll( drawTriangles( model ) );
    children.addAll( drawOptionalLines( model ) );
    children.addAll( drawConnections( model ) );
    mesh = new Group( children );
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Line} as JavaFX {@link Node}
   */
  private List<Node> drawLines( Model model ) {
    Group lineGroup = new Group();
    lineGroup.getChildren().addAll( getLines( model ).stream()
        .map( line -> {
          List<Point3D> points = LineUtils.getVertices( line ).stream()
              .map( ModelMesh::gpbToPoint3D )
              .map( point -> new Point3D( point.x, point.y,
                  point.z ) )
              .toList();
          return new PolyLine3D( points, LINE_WIDTH, LINE_COLOR );
        } )
        .toList() );

    return lineGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Quadrilateral} as JavaFX {@link Node}
   */
  private List<Node> drawQuadrilaterals( Model model ) {
    Group quadrilateralGroup = new Group();

    List<MeshView> quadMeshViews = getQuadrilaterals( model )
        .stream()
        .map( quadrilateral -> {

          List<Vertex> quadrilateralVertices = QuadrilateralUtils.getVertices( quadrilateral );

          float[] quadPointsArray = new float[quadrilateralVertices.size() * 3];

          for ( int i = 0; i < quadrilateralVertices.size(); i++ ) {
            Vertex vertex = quadrilateralVertices.get( i );
            quadPointsArray[3 * i] = Double.valueOf( vertex.getX() ).floatValue();
            quadPointsArray[( 3 * i ) + 1] = Double.valueOf( vertex.getY() ).floatValue();
            quadPointsArray[( 3 * i ) + 2] = Double.valueOf( vertex.getZ() ).floatValue();
          }

          TriangleMesh quadMesh = new TriangleMesh( POINT_TEXCOORD );
          quadMesh.getPoints().addAll( quadPointsArray );
          quadMesh.getTexCoords().addAll( 0, 0 );

          // TODO, [Task] Implement BFC(Back Face Culling) Meta command #29
          quadMesh.getFaces().addAll(
              0, 0,
              1, 0,
              2, 0,
              2, 0,
              3, 0,
              0, 0
          );

          logger.info( "Color: {}", quadrilateral.getColor() );
          MeshView quadView = new MeshView( quadMesh );
          quadView.setMaterial( new PhongMaterial( toJFXColor( quadrilateral.getColor() ) ) );
          quadView.setDrawMode( DrawMode.FILL );
          quadView.setCullFace( CullFace.NONE );

          return quadView;
        } )
        .toList();

    quadrilateralGroup.getChildren().addAll( quadMeshViews );

    return quadrilateralGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.Triangle} as JavaFX {@link Node}
   */
  private List<Node> drawTriangles( Model model ) {
    Group triangleGroup = new Group();

    List<MeshView> triangleMeshViews = getTriangles( model ).stream()
        .map( triangle -> {

          List<Vertex> triangleVertices = TriangleUtils.getVertices( triangle );

          float[] trianglePointsArray = new float[triangleVertices.size() * 3];

          for ( int i = 0; i < triangleVertices.size(); i++ ) {
            Vertex vertex = triangleVertices.get( i );
            trianglePointsArray[3 * i] = Double.valueOf( vertex.getX() ).floatValue();
            trianglePointsArray[( 3 * i ) + 1] = Double.valueOf( vertex.getY() ).floatValue();
            trianglePointsArray[( 3 * i ) + 2] = Double.valueOf( vertex.getZ() ).floatValue();
          }

          TriangleMesh triangleMesh = new TriangleMesh( POINT_TEXCOORD );
          triangleMesh.getPoints().addAll( trianglePointsArray );
          triangleMesh.getTexCoords().addAll( 0, 0 );
          triangleMesh.getFaces().addAll(
              0, 0, 1, 0, 2, 0
          );

          MeshView triangleView = new MeshView( triangleMesh );
          triangleView.setMaterial( new PhongMaterial( toJFXColor( triangle.getColor() ) ) );
          triangleView.setDrawMode( DrawMode.FILL );
          triangleView.setCullFace( CullFace.NONE );

          return triangleView;
        } )
        .toList();

    triangleGroup.getChildren().addAll( triangleMeshViews );
    return triangleGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.primitive.OptionalLine} as JavaFX {@link Node}
   */
  private List<Node> drawOptionalLines( Model model ) {
    Group optionalLineGroup = new Group();

    optionalLineGroup.getChildren().addAll(
        getOptionalLines( model ).stream()
            .map( optionalLine -> {
              List<Point3D> points = getVertices( optionalLine ).stream()
                  .map( ModelMesh::gpbToPoint3D )
                  .map( point -> new Point3D( point.x, point.y,
                      point.z ) )
                  .collect( Collectors.toList() );

              // Add first point again to close loop
              points.add( points.getFirst() );

              return new PolyLine3D( points, LINE_WIDTH, OPTIONAL_LINE_COLOR );
            } )
            .toList() );

    return optionalLineGroup.getChildren();
  }

  /**
   * @param model gpb {@link Model}
   * @return {@link less.lgeo.connectivity.Connection} as JavaFX {@link Node}
   */
  private List<Node> drawConnections( Model model ) {
    Group connectionGroup = new Group();

    Set<Vertex> vertexSet = getConnections( model ).stream()
        .flatMap( connection -> getConnectionPoints( connection ).stream() )
        .collect( Collectors.toSet() );

    for ( Vertex vertex : vertexSet ) {
      CubeMesh connectionPoint = new CubeMesh( CONN_SIZE );
      connectionPoint.setTranslateX( vertex.getX() );
      connectionPoint.setTranslateY( vertex.getY() );
      connectionPoint.setTranslateZ( vertex.getZ() );
      connectionPoint.setMaterial( new PhongMaterial( CONN_COLOR ) );
      connectionGroup.getChildren().add( connectionPoint );
    }

    return connectionGroup.getChildren();
  }


  private Color toJFXColor( less.lgeo.common.Color color ) {
    java.awt.Color modelColor = java.awt.Color.decode( color.getValue() );
    return Color.rgb( modelColor.getRed(), modelColor.getGreen(), modelColor.getBlue() );
  }

}
