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
import less.lgeo.embedded.Vector3Embeddable;
import less.lgeo.primitive.Line;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_lines")
public class LineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_line_seq_gen")
  @SequenceGenerator(
      name = "model_line_seq_gen",
      sequenceName = "model_line_seq",
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
  private Vector3Embeddable p1;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p2_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p2_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p2_z"))
  })
  private Vector3Embeddable p2;


  public static Line toGpb(LineEntity lineEntity) {
    return Line.newBuilder()
        .setType(LineType.LINE)
        .setColorId(lineEntity.getColor().getId())
        .setP1(Vector3Embeddable.toGpb(lineEntity.getP1()))
        .setP2(Vector3Embeddable.toGpb(lineEntity.getP2()))
        .build();
  }
}
