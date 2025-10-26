package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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


    public static Line toPojo(LineEntity lineEntity) {
        return new Line(
                lineEntity.getColor().getId(),
                lineEntity.getP1().toVector3(),
                lineEntity.getP2().toVector3()
        );
    }
}
