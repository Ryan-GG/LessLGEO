package less.lgeo.primitive;

import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;
import org.ejml.data.DMatrix4x4;
import org.ejml.dense.fixed.CommonOps_DDF4;

import java.util.Optional;

import static less.lgeo.common.CommonUtils.getColor;
import static less.lgeo.common.Matrix.dMatrixToMatrix;
import static less.lgeo.common.Matrix.matrixToDMatrix;

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
            DMatrix4x4 result = new DMatrix4x4();
            CommonOps_DDF4.mult(matrixToDMatrix(transformationMatrix.get()),
                    matrixToDMatrix(matrix),
                    result);
            resulted = dMatrixToMatrix(result);
        } else {
            resulted = matrix;
        }

        Color subPartColor = getColor(inheritedColor, color);

        return new SubFileReference(
                subPartColor,
                Matrix.IDENTITY_MATRIX,
                subModel().transformModel(Optional.of(resulted), Optional.of(subPartColor)),
                fileName(),
                pieceConnection().map(connection -> connection.transformConnection(resulted)));
    }
}
