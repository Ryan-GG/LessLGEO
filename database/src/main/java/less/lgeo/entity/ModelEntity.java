package less.lgeo.entity;

import jakarta.persistence.*;
import less.lgeo.embedded.ModelId;
import less.lgeo.primitive.Model;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.lang.NonNull;

import java.util.List;


/**
 * ModelEntity is a joined representation of 'entities' of complex
 * objects representing a {@link Model}. These 'entities' are treated a separate
 * tables which are joined by the model id
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
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

    @BatchSize(size = 500)
    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SubFileRefEntity> pieces;

    public ModelEntity(
            ModelId modelId,
            List<LineEntity> lines,
            List<TriangleEntity> triangles,
            List<QuadrilateralEntity> quadrilaterals,
            List<OptionalLineEntity> optionalLines,
            List<SubFileRefEntity> pieces) {
        this.id = modelId.getValue();
        this.modelId = modelId;
        this.lines = lines;
        this.triangles = triangles;
        this.quadrilaterals = quadrilaterals;
        this.optionalLines = optionalLines;
        this.pieces = pieces;
    }


    public ModelId getId() {
        return ModelId.of(id);
    }

    public void setId(@NonNull ModelId id) {
        this.id = id.getValue();
    }

}
