package less.lgeo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.primitive.Triangle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_triangles")
public class TriangleEntity {

  @Id
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "model_id", nullable = false, unique = false)
  private ModelEntity model;

  @OneToOne
  @JoinColumn(name = "color_id", referencedColumnName = "id")
  private ColorEntity color;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p1_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p1_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p1_z"))
  })
  private VertexEmbeddable p1;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p2_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p2_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p2_z"))
  })
  private VertexEmbeddable p2;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p3_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p3_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p3_z"))
  })
  private VertexEmbeddable p3;

  public TriangleEntity(ColorEntity color, VertexEmbeddable p1, VertexEmbeddable p2,
      VertexEmbeddable p3) {
    this.color = color;
    this.p1 = p1;
    this.p2 = p2;
    this.p3 = p3;
  }

  public static TriangleEntity toEntity(Triangle triangle) {
    return new TriangleEntity(
        new ColorEntity(triangle.getColorId()),
        new VertexEmbeddable(triangle.getP1()),
        new VertexEmbeddable(triangle.getP2()),
        new VertexEmbeddable(triangle.getP3())
    );
  }
}
