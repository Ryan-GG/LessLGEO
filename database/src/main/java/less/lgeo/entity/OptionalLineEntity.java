package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import less.lgeo.embedded.VertexEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model_optional_lines")
public class OptionalLineEntity {

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "p4_x")),
            @AttributeOverride(name = "y", column = @Column(name = "p4_y")),
            @AttributeOverride(name = "z", column = @Column(name = "p4_z"))
    })
    private VertexEmbeddable p4;

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getColor(), getP1(), getP2(), getP3(), getP4());
    }

}
