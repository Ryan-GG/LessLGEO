package less.lgeo.primitive;

import static less.lgeo.common.CommonUtils.dMatrixToGpb;
import static less.lgeo.common.CommonUtils.getColor;
import static less.lgeo.common.CommonUtils.gpbToDMatrix;
import static less.lgeo.connection.ConnectionUtils.transformConnection;
import static less.lgeo.primitive.LineUtils.transformLine;
import static less.lgeo.primitive.OptionalLineUtils.transformOptionalLine;
import static less.lgeo.primitive.QuadrilateralUtils.transformQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.transformTriangle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import less.lgeo.common.Matrix;
import less.lgeo.connectivity.Connection;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;

public class ModelUtils {

  public static final Matrix IDENTITY_MATRIX = Matrix.newBuilder()
      .setA( 1 )
      .setE( 1 )
      .setI( 1 )
      .setScale( 1 )
      .build();

  /**
   * @param model gpb {@link Model}
   * @return All {@link Line} from the Parent Model
   */
  public static Set<Line> getLines( Model model ) {

    Set<Line> lines = new HashSet<>( model.getLineList() );

    model.getPieceList()
        .forEach( subFileReference -> lines.addAll( getLines( subFileReference.getSubModel() ) ) );

    return lines;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Triangle} from the Parent Model
   */
  public static Set<Triangle> getTriangles( Model model ) {

    Set<Triangle> triangles = new HashSet<>( model.getTriangleList() );

    model.getPieceList()
        .forEach(
            subFileReference -> triangles.addAll(
                getTriangles( subFileReference.getSubModel() ) ) );

    return triangles;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Quadrilateral} from the Parent Model
   */
  public static Set<Quadrilateral> getQuadrilaterals( Model model ) {

    Set<Quadrilateral> quadrilaterals = new HashSet<>( model.getQuadrilateralList() );

    model.getPieceList()
        .forEach(
            subFileReference -> quadrilaterals.addAll(
                getQuadrilaterals( subFileReference.getSubModel() ) ) );

    return quadrilaterals;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link OptionalLine} from the Parent Model
   */
  public static Set<OptionalLine> getOptionalLines( Model model ) {

    Set<OptionalLine> optionalLines = new HashSet<>( model.getOptionalLineList() );

    model.getPieceList()
        .forEach(
            subFileReference -> optionalLines.addAll(
                getOptionalLines( subFileReference.getSubModel() ) ) );

    return optionalLines;
  }

  /**
   * @param model gpb {@link Model}
   * @return All {@link Connection} from the Parent Model
   */
  public static Set<Connection> getConnections( Model model ) {

    Set<Connection> connections = new HashSet<>(
        model.getPieceList().stream().map(
                SubFileReference::getPieceConnection )
            .toList()
    );

    model.getPieceList()
        .forEach(
            subFileReference -> connections.addAll(
                getConnections( subFileReference.getSubModel() ) ) );

    return connections;
  }


  public static Model transformModel( Model model ) {
    return transformModel( model, Optional.empty(), Optional.empty()
    );
  }

  private static Model transformModel( Model model, Optional<Matrix> transformationMatrix,
      Optional<Integer> parentColor ) {

    List<Line> transformedLines =
        model.getLineList().stream()
            .map( line -> transformLine( line, transformationMatrix, parentColor ) )
            .toList();

    List<Triangle> transformedTriangles =
        model.getTriangleList().stream()
            .map( triangle -> transformTriangle( triangle, transformationMatrix, parentColor ) )
            .toList();

    List<Quadrilateral> transformedQuadrilaterals =
        model.getQuadrilateralList().stream()
            .map( quadrilateral -> transformQuadrilateral( quadrilateral, transformationMatrix,
                parentColor ) )
            .toList();

    List<OptionalLine> transformedOptionalLines =
        model.getOptionalLineList().stream()
            .map( optionalLine -> transformOptionalLine( optionalLine, transformationMatrix,
                parentColor ) )
            .toList();

    List<SubFileReference> transformedPieces =
        model.getPieceList()
            .stream()
            .map( subFileReference -> {
              // Prepare output matrix
              Matrix resulted = subFileReference.getMatrix();

              if ( transformationMatrix.isPresent() ) {
                DMatrix4x4 result = new DMatrix4x4();
                CommonOps_DDF4.mult( gpbToDMatrix( transformationMatrix.get() ),
                    gpbToDMatrix( subFileReference.getMatrix() ),
                    result );
                resulted = dMatrixToGpb( result );
              }

              int subPartColorId = getColor( parentColor, subFileReference.getColorId() );

              return SubFileReference.newBuilder()
                  .setFileName( subFileReference.getFileName() )
                  .setPieceConnection(
                      transformConnection( subFileReference.getPieceConnection(), resulted ) )
                  .setColorId( subPartColorId )
                  .setMatrix( IDENTITY_MATRIX )
                  .setSubModel(
                      transformModel(
                          subFileReference.getSubModel(),
                          Optional.of( resulted ),
                          Optional.of( subPartColorId )
                      )
                  )
                  .build();
            } )
            .toList();

    return Model.newBuilder()
        .setUUID( model.getUUID() )
        .addAllComment( model.getCommentList() )
        .addAllCommand( model.getCommandList() )
        .addAllLine( transformedLines )
        .addAllTriangle( transformedTriangles )
        .addAllQuadrilateral( transformedQuadrilaterals )
        .addAllOptionalLine( transformedOptionalLines )
        .addAllPiece( transformedPieces )
        .build();
  }

}
