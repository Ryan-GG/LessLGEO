package less.lgeo.embedded;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import less.lgeo.primitive.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LineEmbeddable {

  private int colorId;

  @Embedded
  @AttributeOverrides( {
      @AttributeOverride( name = "x", column = @Column( name = "p1_x" ) ),
      @AttributeOverride( name = "y", column = @Column( name = "p1_y" ) ),
      @AttributeOverride( name = "z", column = @Column( name = "p1_z" ) )
  } )
  private VertexEmbeddable p1;

  @Embedded
  @AttributeOverrides( {
      @AttributeOverride( name = "x", column = @Column( name = "p2_x" ) ),
      @AttributeOverride( name = "y", column = @Column( name = "p2_y" ) ),
      @AttributeOverride( name = "z", column = @Column( name = "p2_z" ) )
  } )
  private VertexEmbeddable p2;

  public static LineEmbeddable fromGpb( Line line ) {
    return new LineEmbeddable(
        line.getColorId(),
        new VertexEmbeddable( line.getP1() ),
        new VertexEmbeddable( line.getP2() ) );
  }
}
