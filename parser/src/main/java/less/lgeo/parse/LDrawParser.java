package less.lgeo.parse;

import static less.lgeo.ParseUtils.isMetaCommand;
import static less.lgeo.ParseUtils.parseCommand;
import static less.lgeo.ParseUtils.parseComment;
import static less.lgeo.ParseUtils.toDouble;
import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.common.VertexUtils.getPoint;
import static less.lgeo.primitive.LineUtils.getLine;
import static less.lgeo.primitive.ModelUtils.transformModel;
import static less.lgeo.primitive.OptionalLineUtils.getOptionalLine;
import static less.lgeo.primitive.QuadrilateralUtils.getQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.getTriangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vertex;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class LDrawParser implements Parser<Model> {

  private static final Logger logger = LoggerFactory.getLogger( LDrawParser.class );

  private final Map<String, Model> modelCache;

  public LDrawParser() {
    this.modelCache = new ConcurrentHashMap<>();
  }

  public Model parse( File toParse ) throws IOException {
    Model parentModel = parseAllModels( toParse );
    return transformModel( parentModel );
  }

  @Override
  public File writeToFile( Model gpb, String fileName ) {
    // TODO, [Task] Add export back to .ldr format of a Model file #24
    return new File( fileName );
  }


  private Model parseAllModels( File toParse ) throws IOException {
    Model.Builder modelBuilder = Model.newBuilder();

    try ( BufferedReader bufferedReader = new BufferedReader(
        new FileReader( toParse, StandardCharsets.UTF_8 ) ) ) {

      logger.info( "Parsing file name: {}", toParse );
      AtomicInteger lineNumber = new AtomicInteger();

      bufferedReader.lines().filter( StringUtils::hasText ).forEach( line -> {
        List<String> values = new ArrayList<>( List.of( line.trim().split( " " ) ) );
        int commandValue = Integer.parseInt( values.removeFirst() );

        LineType lineType = getLineType( commandValue );

        switch ( lineType ) {
          case COMMENT_OR_META_CMD -> {
            if ( values.isEmpty() ) {
              logger.warn( "Found '0' line" );
            } else if ( isMetaCommand( values ) ) {
              modelBuilder.addCommand( parseCommand( values ) );
            } else {
              modelBuilder.addComment( parseComment( lineNumber.get(), values ) );
            }
          }
          case SUB_FILE_REF -> modelBuilder.addPiece( parseSubFileReference( values ) );
          case LINE -> modelBuilder.addLine( parseLine( values ) );
          case TRIANGLE -> modelBuilder.addTriangle( parseTriangle( values ) );
          case QUADRILATERAL -> modelBuilder.addQuadrilateral( parseQuadrilateral( values ) );
          case OPTIONAL_LINE -> modelBuilder.addOptionalLine( parseOptionalLine( values ) );
          default -> throw new IllegalStateException(
              "Line Type has an Illegal type of " + lineType.getDescriptorForType().toString() );
        }
        lineNumber.getAndIncrement();
      } );
      logger.info( "Finished Parsing" );
      return modelBuilder.build();
    }
  }


  /**
   * Converts a list of strings to a {@link SubFileReference}
   *
   * @param values values to parse
   * @return parsed LDraw {@link SubFileReference}
   */
  private SubFileReference parseSubFileReference( List<String> values ) {
    if ( values.size() != 14 ) {
      throw new IllegalStateException(
          "Remaining Sub File Reference files does not match format, size is " + values.size() );
    }

    Color color = parseColor( values.removeFirst() );

    double x = toDouble( values.removeFirst() );
    double y = toDouble( values.removeFirst() );
    double z = toDouble( values.removeFirst() );
    double a = toDouble( values.removeFirst() );
    double b = toDouble( values.removeFirst() );
    double c = toDouble( values.removeFirst() );
    double d = toDouble( values.removeFirst() );
    double e = toDouble( values.removeFirst() );
    double f = toDouble( values.removeFirst() );
    double g = toDouble( values.removeFirst() );
    double h = toDouble( values.removeFirst() );
    double i = toDouble( values.removeFirst() );

    Matrix parsedMatrix = Matrix.newBuilder()
        .setX( x )
        .setY( y )
        .setZ( z )
        .setA( a )
        .setB( b )
        .setC( c )
        .setD( d )
        .setE( e )
        .setF( f )
        .setG( g )
        .setH( h )
        .setI( i )
        .setScale( 1.0 )
        .build();



    /*
     * FIXME
     * this recursive parsing is not good, as it will find the first instance of a matching file name,
     * possibly not find the correct directory one. Why there are multiple .dat files with the same name IDK
     */
    List<String> subFileParts = Arrays.stream( values.getFirst().split( "\\\\" ) ).toList();
    String subFileName = subFileParts.getLast();
    Model parsedSubFileModel = getParsedSubFileModel( subFileName );
    return SubFileReference.newBuilder()
        .setFileName( subFileName )
        .setType( LineType.SUB_FILE_REF )
        .setColor(
            Color.getDefaultInstance()
        )
        .setMatrix( parsedMatrix )
        .setSubModel( parsedSubFileModel )
        .build();
  }

  private @NotNull Model getParsedSubFileModel( String subFileName ) {
    // split on \, and use last as file name to search for
    if ( this.modelCache.containsKey( subFileName ) ) {
      return this.modelCache.get( subFileName );
    }

    logger.info( "Searching for subFile: {}", subFileName );
    try ( Stream<Path> ldrawDir = Files.walk( Path.of( "ldraw" ) ) ) {

      Optional<Path> subFilePath = ldrawDir.filter(
              path -> path.getFileName().toString().equals( subFileName ) )
          .findFirst();

      if ( subFilePath.isEmpty() ) {
        throw new IOException();
      }

      Model parsedSubModel = parse( subFilePath.get().toFile() );
      this.modelCache.put( subFileName, parsedSubModel );
      return parsedSubModel;

    } catch ( IOException ex ) {
      logger.error( "Sub file does not exist, {}", subFileName );
      throw new IllegalStateException(
          "Parsed Sub File Model is null, failed trying to parse " + subFileName );
    }
  }

  /**
   * @return parsed LDraw {@link Line}
   */
  private Line parseLine( List<String> values ) {
    if ( values.size() != 7 ) {
      throw new IllegalStateException(
          "Remaining Line does not match format, size is " + values.size() );
    }
    Color color = parseColor( values.removeFirst() );
    double x1 = toDouble( values.removeFirst() );
    double y1 = toDouble( values.removeFirst() );
    double z1 = toDouble( values.removeFirst() );
    double x2 = toDouble( values.removeFirst() );
    double y2 = toDouble( values.removeFirst() );
    double z2 = toDouble( values.removeFirst() );

    Vertex p1 = getPoint( x1, y1, z1 );
    Vertex p2 = getPoint( x2, y2, z2 );
    return getLine(
        color,
        p1,
        p2
    );
  }

  /**
   * @return parsed LDraw {@link Triangle}
   */
  private Triangle parseTriangle( List<String> values ) {
    if ( values.size() != 10 ) {
      throw new IllegalStateException(
          "Remaining Triangle does not match format, size is " + values.size() );
    }
    Color color = parseColor( values.removeFirst() );
    double x1 = toDouble( values.removeFirst() );
    double y1 = toDouble( values.removeFirst() );
    double z1 = toDouble( values.removeFirst() );
    double x2 = toDouble( values.removeFirst() );
    double y2 = toDouble( values.removeFirst() );
    double z2 = toDouble( values.removeFirst() );
    double x3 = toDouble( values.removeFirst() );
    double y3 = toDouble( values.removeFirst() );
    double z3 = toDouble( values.removeFirst() );

    Vertex p1 = getPoint( x1, y1, z1 );
    Vertex p2 = getPoint( x2, y2, z2 );
    Vertex p3 = getPoint( x3, y3, z3 );
    return getTriangle(
        color,
        p1,
        p2,
        p3
    );
  }

  /**
   * @return parsed LDraw {@link Quadrilateral}
   */
  private Quadrilateral parseQuadrilateral( List<String> values ) {
    if ( values.size() != 13 ) {
      throw new IllegalStateException(
          "Remaining Quadrilateral does not match format, size is " + values.size() );
    }
    Color color = parseColor( values.removeFirst() );
    double x1 = toDouble( values.removeFirst() );
    double y1 = toDouble( values.removeFirst() );
    double z1 = toDouble( values.removeFirst() );
    double x2 = toDouble( values.removeFirst() );
    double y2 = toDouble( values.removeFirst() );
    double z2 = toDouble( values.removeFirst() );
    double x3 = toDouble( values.removeFirst() );
    double y3 = toDouble( values.removeFirst() );
    double z3 = toDouble( values.removeFirst() );
    double x4 = toDouble( values.removeFirst() );
    double y4 = toDouble( values.removeFirst() );
    double z4 = toDouble( values.removeFirst() );

    Vertex p1 = getPoint( x1, y1, z1 );
    Vertex p2 = getPoint( x2, y2, z2 );
    Vertex p3 = getPoint( x3, y3, z3 );
    Vertex p4 = getPoint( x4, y4, z4 );
    return getQuadrilateral(
        color,
        p1,
        p2,
        p3,
        p4
    );
  }

  /**
   * @return parsed LDraw {@link OptionalLine}
   */
  private OptionalLine parseOptionalLine( List<String> values ) {
    if ( values.size() != 13 ) {
      throw new IllegalStateException(
          "Remaining Optional Line does not match format, size is " + values.size() );
    }
    Color color = parseColor( values.removeFirst() );
    double x1 = toDouble( values.removeFirst() );
    double y1 = toDouble( values.removeFirst() );
    double z1 = toDouble( values.removeFirst() );
    double x2 = toDouble( values.removeFirst() );
    double y2 = toDouble( values.removeFirst() );
    double z2 = toDouble( values.removeFirst() );
    double x3 = toDouble( values.removeFirst() );
    double y3 = toDouble( values.removeFirst() );
    double z3 = toDouble( values.removeFirst() );
    double x4 = toDouble( values.removeFirst() );
    double y4 = toDouble( values.removeFirst() );
    double z4 = toDouble( values.removeFirst() );

    Vertex p1 = getPoint( x1, y1, z1 );
    Vertex p2 = getPoint( x2, y2, z2 );
    Vertex p3 = getPoint( x3, y3, z3 );
    Vertex p4 = getPoint( x4, y4, z4 );
    return getOptionalLine(
        color,
        p1,
        p2,
        p3,
        p4
    );
  }

  /**
   * @return parsed LDraw {@link Color}
   */
  private Color parseColor( String color ) {
    return Color.getDefaultInstance();
  }

}
