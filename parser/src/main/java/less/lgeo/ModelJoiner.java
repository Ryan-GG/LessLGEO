package less.lgeo;

import static less.lgeo.common.CommonUtils.PART_EXT;
import static less.lgeo.common.CommonUtils.changeFileExtension;
import static less.lgeo.primitive.ModelUtils.transformModel;

import jakarta.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import less.lgeo.connectivity.Connection;
import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ModelJoiner {

  private static final Logger logger = LoggerFactory.getLogger( ModelJoiner.class );
  private final LDrawParser lDrawParser;
  private final ConnectivityParser connectivityParser;

  public ModelJoiner(
      LDrawParser lDrawParser,
      ConnectivityParser connectivityParser
  ) {
    this.lDrawParser = lDrawParser;
    this.connectivityParser = connectivityParser;
  }

  public Model joinAndTransformModel( File fileToParse ) {

    Model parentModel = getLDrawModel( fileToParse );
    if ( parentModel != null ) {
      List<SubFileReference> connectedPieces = parentModel.getPieceList().stream()
          .map( this::getPieceWithConnection )
          .toList();

      parentModel = parentModel.toBuilder()
          .clearPiece()
          .addAllPiece( connectedPieces )
          .build();
    }

    // FIXME, need to transform connections respective to piece
    return transformModel( parentModel );
  }

  private @Nullable Model getLDrawModel( File toParse ) {
    try {
      return lDrawParser.parse( toParse );
    } catch ( IOException e ) {
      logger.error( "Failed to parse LDraw File {}", toParse.getAbsolutePath() );
    }
    return null;
  }

  private @Nullable SubFileReference getPieceWithConnection( SubFileReference piece ) {
    File connectionFile = new File( "connectivity",
        changeFileExtension( piece.getFileName(), PART_EXT ) );

    try {
      Connection pieceConnection = connectivityParser.parse( connectionFile );

      return piece.toBuilder()
          .setPieceConnection( pieceConnection )
          .build();
    } catch ( IOException e ) {
      logger.error( "Failed to join Connectivity File {}", connectionFile.getAbsolutePath() );
    }
    logger.warn( "Piece Connection is Null" );
    return null;
  }
}
