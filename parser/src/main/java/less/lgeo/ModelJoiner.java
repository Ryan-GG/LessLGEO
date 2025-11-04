package less.lgeo;

import less.lgeo.connection.Connection;
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
import java.util.Optional;

import static less.lgeo.connection.Connection.PART_EXT;
import static less.lgeo.connection.Connection.changeFileExtension;

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

    public Model joinAndTransformModel(String lDraw) {

        Model parentModel = getLDrawModel(lDraw);

        List<SubFileReference> connectedPieces = parentModel.pieces().stream()
                .map(this::joinPieceWithConnection)
                .toList();

        if (!connectedPieces.isEmpty()) {
            parentModel = new Model(
                    parentModel.comments(),
                    parentModel.commands(),
                    parentModel.lines(),
                    parentModel.triangles(),
                    parentModel.quadrilaterals(),
                    parentModel.optionalLines(),
                    connectedPieces
            );
        }

        return parentModel.transformModel();
    }

    private @NonNull Model getLDrawModel(String lDraw) {
        return lDrawParser.parse(lDraw);
    }

    private @NonNull SubFileReference joinPieceWithConnection(SubFileReference piece) {

        File connectionFile = new File("connectivity",
                changeFileExtension(piece.fileName(), PART_EXT));

        if (Files.exists(connectionFile.toPath())) {
            try {
                String input = Files.readString(connectionFile.toPath());

                Connection pieceConnection = connectivityParser.parse(input);

                return new SubFileReference(
                        piece.color(),
                        piece.matrix(),
                        piece.subModel(),
                        piece.fileName(),
                        Optional.of(pieceConnection)
                );
            } catch (IOException e) {
                logger.error("Failed to open connectivity file {}", connectionFile.getAbsolutePath());
            }
        }

        logger.warn("Connection file does not exist at {}, connection is null",
                connectionFile.getAbsolutePath());
        return piece;
    }
}
