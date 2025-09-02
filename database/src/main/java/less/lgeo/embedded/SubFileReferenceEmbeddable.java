package less.lgeo.embedded;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import less.lgeo.primitive.SubFileReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SubFileReferenceEmbeddable {

  private int colorId;

  @Embedded
  private MatrixEmbeddable matrix;

  // ref id to another model

  private String fileName;

  // TODO, private Connection when i get to it

  public SubFileReferenceEmbeddable fromGpb( SubFileReference subFileReference ) {
    return new SubFileReferenceEmbeddable(
        subFileReference.getColorId(),
        new MatrixEmbeddable( subFileReference.getMatrix() ),
        subFileReference.getFileName()
    );
  }

}
