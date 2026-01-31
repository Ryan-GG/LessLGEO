package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;
import org.joml.Matrix4d;

import java.util.Optional;

public record SubFileReference(
        Color color,
        Matrix matrix,
        Model subModel,
        String fileName,
        Optional<Connection> pieceConnection) {

    public static final LineType type = LineType.SUB_FILE_REF;

    public SubFileReference transform(
            Optional<Matrix> transformationMatrix,
            Optional<Color> inheritedColor) {
        final Matrix resulted;

        if (transformationMatrix.isPresent()) {
            Matrix4d result = new Matrix4d();

            transformationMatrix.get().toMatrix4d()
                    .mul(matrix.toMatrix4d(), result);
            
            resulted = new Matrix(result);
        } else {
            resulted = matrix;
        }

        Color subPartColor = color.inheritColor(inheritedColor);

        return new SubFileReference(
                subPartColor,
                Matrix.IDENTITY_MATRIX,
                subModel().transformModel(Optional.of(resulted), Optional.of(subPartColor)),
                fileName(),
                pieceConnection().map(connection -> connection.transformConnection(resulted)));
    }
}
