package less.lgeo.parse;

import static less.lgeo.ParseUtils.isMetaCommand;
import static less.lgeo.ParseUtils.parseCommand;
import static less.lgeo.ParseUtils.parseComment;
import static less.lgeo.ParseUtils.toDouble;
import static less.lgeo.primitive.PrimitiveUtils.getLineType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.EighthBlock;
import less.lgeo.connectivity.PartConnection;
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
    logger.info( "Connection: {}", connectivityParser.parse( toParse ) );
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

        logger.info( "Parsing line {} : {}", lineNumber, line );
        int commandValue = Integer.parseInt( values.removeFirst() );
        LineType lineType = getLineType( commandValue );

        if ( lineType == LineType.COMMENT_OR_META_CMD ) {
          if ( values.isEmpty() ) {
            logger.warn( "Found '0' line" );
          } else if ( isMetaCommand( values ) ) {
            connectionBuilder.addCommand( parseCommand( values ) );
          } else {
            connectionBuilder.addComment( parseComment( lineNumber.get(), values ) );
          }
        } else {
          throw new IllegalStateException( "Unexpected Line Type" );
        }
      } );

      connectionBuilder.build().getCommandList().forEach( command ->
      {
        if ( command.getCommand().equals( "PE_CONN" ) ) {
          Iterator<String> additionalParamsIter = command.getAdditionalParamsList().iterator();
          connectionBuilder.addPartConnection(
              PartConnection.newBuilder()
                  .setGroupId( Integer.parseInt( additionalParamsIter.next() ) )
                  .setElementId( Integer.parseInt( additionalParamsIter.next() ) )
                  .setMatrix(
                      Matrix.newBuilder()
                          .setX( toDouble( additionalParamsIter.next() ) )
                          .setY( toDouble( additionalParamsIter.next() ) )
                          .setZ( toDouble( additionalParamsIter.next() ) )
                          .setA( toDouble( additionalParamsIter.next() ) )
                          .setB( toDouble( additionalParamsIter.next() ) )
                          .setC( toDouble( additionalParamsIter.next() ) )
                          .setD( toDouble( additionalParamsIter.next() ) )
                          .setE( toDouble( additionalParamsIter.next() ) )
                          .setF( toDouble( additionalParamsIter.next() ) )
                          .setG( toDouble( additionalParamsIter.next() ) )
                          .setH( toDouble( additionalParamsIter.next() ) )
                          .setI( toDouble( additionalParamsIter.next() ) )
                          .setScale( 1.0 )
                  )
                  .setGeometryData( parseSeventhBlock( additionalParamsIter ) )
                  .setVisualGeometry( parseEighthBlock( additionalParamsIter ) )
          );
        }
      } );
      return connectionBuilder.build();
    }
  }


  private SeventhBlock parseSeventhBlock( Iterator<String> values ) {
    return SeventhBlock.newBuilder()
        .setUnknown( values.next() )
        .setUnknown2( values.next() )
        .build();
  }

  private EighthBlock parseEighthBlock( Iterator<String> values ) {
    return EighthBlock.newBuilder()
        .setUnknown( values.next() )
        .build();
  }

  @Override
  public File writeToFile( Connection gpb, String fileName ) {
    // TODO
    return null;
  }
}
