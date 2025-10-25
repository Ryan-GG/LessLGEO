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
import less.lgeo.primitive.OptionalLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_optional_lines")
public class OptionalLineEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_optional_line_seq_gen")
  @SequenceGenerator(
      name = "model_optional_line_seq_gen",
      sequenceName = "model_optional_line_seq",
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

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p3_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p3_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p3_z"))
  })
  private Vector3Embeddable p3;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "x", column = @Column(name = "p4_x")),
      @AttributeOverride(name = "y", column = @Column(name = "p4_y")),
      @AttributeOverride(name = "z", column = @Column(name = "p4_z"))
  })
  private Vector3Embeddable p4;


  public static OptionalLine toGpb(OptionalLineEntity optionalLineEntity) {
    return OptionalLine.newBuilder()
        .setType(LineType.OPTIONAL_LINE)
        .setColorId(optionalLineEntity.getColor().getId())
        .setP1(Vector3Embeddable.toGpb(optionalLineEntity.getP1()))
        .setP2(Vector3Embeddable.toGpb(optionalLineEntity.getP2()))
        .setP3(Vector3Embeddable.toGpb(optionalLineEntity.getP3()))
        .setP4(Vector3Embeddable.toGpb(optionalLineEntity.getP4()))
        .build();
  }

}
