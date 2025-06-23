package less.lgeo;

import static less.lgeo.common.CommonUtils.PART_EXT;
import static less.lgeo.common.CommonUtils.changeFileExtension;
import static less.lgeo.primitive.ModelUtils.transformModel;

import jakarta.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import less.lgeo.connectivity.Connection;
import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Provides parsing, transforming, and joining of .dat and .part Currently parses a file one by one
 * but is possible to do in parallel if recognize files before GPB creation
 */
@Component
public class ModelJoiner {

  private static final Logger logger = LoggerFactory.getLogger(ModelJoiner.class);
  private final LDrawParser lDrawParser;
  private final ConnectivityParser connectivityParser;

  public ModelJoiner(
      LDrawParser lDrawParser,
      ConnectivityParser connectivityParser
  ) {
    this.lDrawParser = lDrawParser;
    this.connectivityParser = connectivityParser;
  }

  public Model joinAndTransformModel(String toParse) {

    Model parentModel = getLDrawModel(toParse);

    if (parentModel != null) {
      List<SubFileReference> connectedPieces = parentModel.getPieceList().stream()
          .map(this::getPieceWithConnection)
          .toList();

      parentModel = parentModel.toBuilder()
          .clearPiece()
          .addAllPiece(connectedPieces)
          .build();
    }

    return transformModel(parentModel);
  }

  private @Nullable Model getLDrawModel(String toParse) {
    return lDrawParser.parse(toParse);
  }

  private @Nullable SubFileReference getPieceWithConnection(SubFileReference piece) {

    File connectionFile = new File("connectivity",
        changeFileExtension(piece.getFileName(), PART_EXT));

    try (BufferedReader reader = new BufferedReader(
        new FileReader(connectionFile, StandardCharsets.UTF_8))) {

      String input = reader.lines().sequential().collect(Collectors.joining());
      Connection pieceConnection = connectivityParser.parse(input);

      return piece.toBuilder()
          .clearPieceConnection()
          .setPieceConnection(pieceConnection)
          .build();
    } catch (IOException e) {
      logger.info("Failed to open connectivity file {}", connectionFile.getAbsolutePath());
    }

    logger.warn("Piece Connection is Null");
    return null;
  }
}
