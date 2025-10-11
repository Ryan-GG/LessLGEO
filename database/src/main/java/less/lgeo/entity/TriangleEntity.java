package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import less.lgeo.common.LineType;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_triangle_seq_gen")
  @SequenceGenerator(
      name = "model_triangle_seq_gen",
      sequenceName = "model_triangle_seq",
      allocationSize = 50
  )
  private Long id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "model_id", nullable = false)
  private ModelEntity model;

  @OneToOne(optional = false)
  @JoinColumn(name = "color_id", referencedColumnName = "id", unique = false)
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


  public static Triangle toGpb(TriangleEntity triangleEntity) {
    return Triangle.newBuilder()
        .setType(LineType.TRIANGLE)
        .setColorId(triangleEntity.getColor().getId())
        .setP1(VertexEmbeddable.toGpb(triangleEntity.getP1()))
        .setP2(VertexEmbeddable.toGpb(triangleEntity.getP2()))
        .setP3(VertexEmbeddable.toGpb(triangleEntity.getP3()))
        .build();
  }

}
