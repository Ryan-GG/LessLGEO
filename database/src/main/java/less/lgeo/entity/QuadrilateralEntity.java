package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import less.lgeo.embedded.Vector3Embeddable;
import less.lgeo.primitive.Quadrilateral;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_quadrilaterals")
public class QuadrilateralEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_quadrilateral_seq_gen")
    @SequenceGenerator(
            name = "model_quadrilateral_seq_gen",
            sequenceName = "model_quadrilateral_seq",
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


    public static Quadrilateral toPojo(QuadrilateralEntity quadrilateralEntity) {
        return new Quadrilateral(
                quadrilateralEntity.getColor().getId(),
                quadrilateralEntity.getP1().toVector3(),
                quadrilateralEntity.getP2().toVector3(),
                quadrilateralEntity.getP3().toVector3(),
                quadrilateralEntity.getP3().toVector3()
        );
    }

}

