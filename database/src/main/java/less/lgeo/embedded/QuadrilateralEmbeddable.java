package less.lgeo.embedded;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import less.lgeo.primitive.Quadrilateral;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class QuadrilateralEmbeddable {

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

  @Embedded
  @AttributeOverrides( {
      @AttributeOverride( name = "x", column = @Column( name = "p3_x" ) ),
      @AttributeOverride( name = "y", column = @Column( name = "p3_y" ) ),
      @AttributeOverride( name = "z", column = @Column( name = "p3_z" ) )
  } )
  private VertexEmbeddable p3;

  @Embedded
  @AttributeOverrides( {
      @AttributeOverride( name = "x", column = @Column( name = "p4_x" ) ),
      @AttributeOverride( name = "y", column = @Column( name = "p4_y" ) ),
      @AttributeOverride( name = "z", column = @Column( name = "p4_z" ) )
  } )
  private VertexEmbeddable p4;

  public static QuadrilateralEmbeddable fromGpb( Quadrilateral quadrilateral ) {
    return new QuadrilateralEmbeddable(
        quadrilateral.getColorId(),
        new VertexEmbeddable( quadrilateral.getP1() ),
        new VertexEmbeddable( quadrilateral.getP2() ),
        new VertexEmbeddable( quadrilateral.getP3() ),
        new VertexEmbeddable( quadrilateral.getP4() )
    );
  }

}
