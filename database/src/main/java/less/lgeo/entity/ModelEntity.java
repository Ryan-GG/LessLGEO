package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import less.lgeo.common.Matrix;
import less.lgeo.embedded.ModelId;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ModelEntity is a joined representation of 'embedded' collections of complex
 * objects representing
 * a {@link Model} proto object. These 'embedded' objects are treated a separate
 * tables which are
 * joined by the model id
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "models")
public class ModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_seq_gen")
    @SequenceGenerator(name = "model_seq_gen", sequenceName = "model_seq", allocationSize = 50)
    private Long id;

    @Transient
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private ModelId modelId;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LineEntity> lines;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TriangleEntity> triangles;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuadrilateralEntity> quadrilaterals;

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OptionalLineEntity> optionalLines;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_id")
    private ModelEntity parent;

    @JsonManagedReference
    @BatchSize(size = 500)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ModelEntity> children = new ArrayList<>();

    public Model toPojo() {
        return new Model(
                List.of(),
                List.of(),
                lines.stream().map(LineEntity::toPojo).toList(),
                triangles.stream().map(TriangleEntity::toPojo).toList(),
                quadrilaterals.stream().map(QuadrilateralEntity::toPojo).toList(),
                optionalLines.stream().map(OptionalLineEntity::toPojo).toList(),
                children.stream().map(ModelEntity::toSubFileRef).toList());
    }

    private SubFileReference toSubFileRef() {
        // FIXME, This seems like the database doesn't match the pojos and is awkward to deal
        // with
        return new SubFileReference(16, Matrix.IDENTITY_MATRIX, this.toPojo(), "", Optional.empty());
    }

    public @Nullable ModelId getId() {
        return id == null ? null : ModelId.of(id);
    }

    public void setId(ModelId id) {
        this.id = id == null ? null : id.getValue();
    }

}
