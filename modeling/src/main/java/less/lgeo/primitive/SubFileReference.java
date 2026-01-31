package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;
import org.joml.Matrix4d;

import java.util.Optional;

import static less.lgeo.common.Matrix.fromMatrix4d;
import static less.lgeo.common.Matrix.toMatrix4d;

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

            toMatrix4d(transformationMatrix.get()).mul(toMatrix4d(matrix), result);

            resulted = fromMatrix4d(result);
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
