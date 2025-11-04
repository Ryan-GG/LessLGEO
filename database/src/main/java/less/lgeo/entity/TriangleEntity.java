package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import less.lgeo.embedded.Vector3dEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    private Vector3dEmbeddable p1;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "p2_x")),
            @AttributeOverride(name = "y", column = @Column(name = "p2_y")),
            @AttributeOverride(name = "z", column = @Column(name = "p2_z"))
    })
    private Vector3dEmbeddable p2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "p3_x")),
            @AttributeOverride(name = "y", column = @Column(name = "p3_y")),
            @AttributeOverride(name = "z", column = @Column(name = "p3_z"))
    })
    private Vector3dEmbeddable p3;

}
