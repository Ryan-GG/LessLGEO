package less.lgeo.parse;

import static less.lgeo.common.CommonUtils.getGroupId;
import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.isMetaCommand;
import static less.lgeo.util.ParseUtils.parseCommand;
import static less.lgeo.util.ParseUtils.parseComment;
import static less.lgeo.util.ParseUtils.toDouble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.Connection.Builder;
import less.lgeo.connectivity.GroupId;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ConnectivityParser implements Parser<Connection> {

  private static final Logger logger = LoggerFactory.getLogger( ConnectivityParser.class );

  public static void main( String[] args ) throws IOException {
    File toParse = new File( args[0] );
    ConnectivityParser connectivityParser = new ConnectivityParser();
    logger.info( "Connection: {}", connectivityParser.parse( toParse ) );
  }

  @Override
  public Connection parse( File fileToParse ) throws IOException {

    Builder connectionBuilder = Connection.newBuilder();

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

          GroupId groupId = getGroupId( Integer.parseInt( additionalParamsIter.next() ) );

          if ( groupId != null ) {
            connectionBuilder.addPartConnection(
                getPartConnection( groupId, additionalParamsIter ) );
          }
        }
      } );
      return connectionBuilder.build();
    }
  }

  private PartConnection getPartConnection( GroupId groupId, Iterator<String> iter ) {

    PartConnection.Builder builder = parseBody( groupId, iter );
    return switch ( groupId ) {
      case GROUP_ZERO -> null;
      case GROUP_ONE -> null;
      case GROUP_STUD -> parseGroupStud( builder, iter );
      case GROUP_FOUR -> null;
      case GROUP_SIX -> null;
      default -> throw new IllegalArgumentException( "Unrecognized Group Id" );
    };
  }

  private PartConnection.Builder parseBody( GroupId groupId, Iterator<String> iterator ) {
    return PartConnection.newBuilder()
        .setGroupId( groupId )
        .setElementId( Integer.parseInt( iterator.next() ) )
        .setMatrix(
            Matrix.newBuilder()
                .setA( toDouble( iterator.next() ) )
                .setB( toDouble( iterator.next() ) )
                .setC( toDouble( iterator.next() ) )
                .setD( toDouble( iterator.next() ) )
                .setE( toDouble( iterator.next() ) )
                .setF( toDouble( iterator.next() ) )
                .setG( toDouble( iterator.next() ) )
                .setH( toDouble( iterator.next() ) )
                .setI( toDouble( iterator.next() ) )
                .setX( toDouble( iterator.next() ) )
                .setY( toDouble( iterator.next() ) )
                .setZ( toDouble( iterator.next() ) )
                .setScale( 1.0 )
        );
  }


  private PartConnection parseGroupStud( PartConnection.Builder builder,
      Iterator<String> iterator ) {
    return builder.setGroupStud(
        GroupStud.newBuilder()
            // TODO this might need to be flipped
            .setZWidthHalfStud( Integer.parseInt( iterator.next() ) )
            .setXWidthHalfStud( Integer.parseInt( iterator.next() ) )
            .setUnknown( iterator.next() )
    ).build();
  }


  @Override
  public File writeToFile( Connection gpb, String fileName ) {
    // TODO
    return null;
  }
}
