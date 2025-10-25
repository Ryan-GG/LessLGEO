package less.lgeo.primitive;

import java.util.Optional;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connection.Connection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubFileReference {

  private final LineType type = LineType.SUB_FILE_REF;
  private final Long id = null;
  private final int colorId;
  private final Matrix matrix;
  private final Model subModel;
  private final String fileName;
  private final Optional<Connection> pieceConnection;
}
