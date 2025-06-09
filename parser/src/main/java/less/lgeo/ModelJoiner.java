package less.lgeo;

import static less.lgeo.common.CommonUtils.PART_EXT;
import static less.lgeo.common.CommonUtils.changeFileExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import less.lgeo.connectivity.Connection;
import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import org.springframework.stereotype.Component;

@Component
public class ModelJoiner {

  private final LDrawParser lDrawParser;
  private final ConnectivityParser connectivityParser;

  public ModelJoiner(
      LDrawParser lDrawParser,
      ConnectivityParser connectivityParser
  ) {
    this.lDrawParser = lDrawParser;
    this.connectivityParser = connectivityParser;
  }

  public Model joinModel( File fileToParse ) throws IOException {

    Model parentModel = lDrawParser.parse( fileToParse );

    List<SubFileReference> connectedPieces = parentModel.getPieceList()
        .stream().map( piece -> {
          try {
            File connectionFile = new File( "connectivity",
                changeFileExtension( piece.getFileName(), PART_EXT ) );

            Connection pieceConnection = connectivityParser.parse( connectionFile );

            return piece.toBuilder()
                .setPieceConnection( pieceConnection )
                .build();

          } catch ( IOException e ) {
            throw new RuntimeException( e );
          }
        } ).toList();

    Model connectedParentModel = Model.newBuilder()
        .addAllComment( parentModel.getCommentList() )
        .addAllCommand( parentModel.getCommandList() )
        .addAllLine( parentModel.getLineList() )
        .addAllTriangle( parentModel.getTriangleList() )
        .addAllQuadrilateral( parentModel.getQuadrilateralList() )
        .addAllOptionalLine( parentModel.getOptionalLineList() )
        .addAllPiece( connectedPieces )
        .build();

    return connectedParentModel;
  }
}
