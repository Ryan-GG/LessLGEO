package less.lgeo.parse;

import static less.lgeo.ParseUtils.toDouble;
import static less.lgeo.primitive.PrimitiveUtils.getLineType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.EighthBlock;
import less.lgeo.connectivity.SeventhBlock;
import less.lgeo.primitive.LineType;
import less.lgeo.primitive.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ConnectivityParser implements Parser<Connection> {

  private static final Logger logger = LoggerFactory.getLogger( ConnectivityParser.class );

  public static void main( String[] args ) throws IOException {
    File toParse = new File( args[0] );
    ConnectivityParser connectivityParser = new ConnectivityParser();
    logger.info( "Connection {}", connectivityParser.parse( toParse ) );
  }

  @Override
  public Connection parse( File fileToParse ) throws IOException {

    Connection.Builder connectionBuilder = Connection.newBuilder();

    try ( BufferedReader bufferedReader = new BufferedReader(
        new FileReader( fileToParse, StandardCharsets.UTF_8 ) ) ) {

      logger.info( "Parsing file name: {}", fileToParse );
      AtomicInteger lineNumber = new AtomicInteger();

      bufferedReader.lines().filter( StringUtils::hasText ).forEach( line -> {
        List<String> values = new ArrayList<>( List.of( line.trim().split( " " ) ) );

        int commandValue = Integer.parseInt( values.removeFirst() );
        LineType lineType = getLineType( commandValue );

        connectionBuilder.setType( lineType );
        connectionBuilder.setCommand( values.removeFirst() );
        connectionBuilder.setGroupId( values.removeFirst() );
        connectionBuilder.setElementId( values.removeFirst() );

        double a = toDouble( values.removeFirst() );
        double b = toDouble( values.removeFirst() );
        double c = toDouble( values.removeFirst() );
        double d = toDouble( values.removeFirst() );
        double e = toDouble( values.removeFirst() );
        double f = toDouble( values.removeFirst() );
        double g = toDouble( values.removeFirst() );
        double h = toDouble( values.removeFirst() );
        double i = toDouble( values.removeFirst() );
        double x = toDouble( values.removeFirst() );
        double y = toDouble( values.removeFirst() );
        double z = toDouble( values.removeFirst() );

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

        connectionBuilder.setMatrix( parsedMatrix )
            .setGeometryData( parseSeventhBlock( values ) )
            .setVisualGeometry( parseEighthBlock( values ) );
      } );
    }

    return connectionBuilder.build();
  }

  private SeventhBlock parseSeventhBlock( List<String> values ) {
    return SeventhBlock.newBuilder()
        .setUnknown( values.removeFirst() )
        .setUnknown2( values.removeFirst() )
        .build();
  }

  private EighthBlock parseEighthBlock( List<String> values ) {
    return EighthBlock.newBuilder()
        .setUnknown( values.removeFirst() )
        .build();
  }

  @Override
  public File writeToFile( Connection gpb, String fileName ) {
    // TODO
    return null;
  }
}
