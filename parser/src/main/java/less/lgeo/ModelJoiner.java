package less.lgeo;

import less.lgeo.connectivity.Connection;
import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static less.lgeo.common.CommonUtils.PART_EXT;
import static less.lgeo.common.CommonUtils.changeFileExtension;
import static less.lgeo.primitive.ModelUtils.transformModel;

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

    public Model joinAndTransformModel(ModelJobRequest modelJobRequest) {

        Model parentModel = getLDrawModel(modelJobRequest);

        List<SubFileReference> connectedPieces = parentModel.getPieceList().stream()
                .map(this::joinPieceWithConnection)
                .toList();

        if (!connectedPieces.isEmpty()) {
            parentModel = parentModel.toBuilder()
                    .clearPiece()
                    .addAllPiece(connectedPieces)
                    .build();
        }

        return transformModel(parentModel);
    }

    private @NonNull Model getLDrawModel(ModelJobRequest modelJobRequest) {
        return lDrawParser.parse(modelJobRequest.getModelString()).toBuilder().build();
    }

    private @NonNull SubFileReference joinPieceWithConnection(SubFileReference piece) {

        File connectionFile = new File("connectivity",
                changeFileExtension(piece.getFileName(), PART_EXT));

        if (Files.exists(connectionFile.toPath())) {
            try {
                String input = Files.readString(connectionFile.toPath());

                Connection pieceConnection = connectivityParser.parse(input);

                return piece.toBuilder()
                        .clearPieceConnection()
                        .setPieceConnection(pieceConnection)
                        .build();
            } catch (IOException e) {
                logger.error("Failed to open connectivity file {}", connectionFile.getAbsolutePath());
            }
        }

        logger.warn("Connection file does not exist at {}, connection is null",
                connectionFile.getAbsolutePath());
        return piece;
    }
}
