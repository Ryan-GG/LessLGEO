package less.lgeo.primitive;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;
import lombok.Data;

import java.util.Optional;

@Data
public class SubFileReference {

    private final LineType type = LineType.SUB_FILE_REF;
    private final int colorId;
    private final Matrix matrix;
    private final Model subModel;
    private final String fileName;
    private final Optional<Connection> pieceConnection;

    public SubFileReference(int colorId, Matrix matrix, Model subModel, String fileName, Optional<Connection> pieceConnection) {
        this.colorId = colorId;
        this.matrix = matrix;
        this.subModel = subModel;
        this.fileName = fileName;
        this.pieceConnection = pieceConnection;
    }
}
