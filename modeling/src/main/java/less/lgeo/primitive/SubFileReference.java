package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;

import java.util.Optional;

public record SubFileReference(
        Color color,
        Matrix matrix,
        Model subModel,
        String fileName,
        Optional<Connection> pieceConnection) {

    private static final LineType type = LineType.SUB_FILE_REF;
}
