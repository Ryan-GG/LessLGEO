package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import less.lgeo.common.LineType;
import less.lgeo.embedded.ModelId;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.SubFileReference;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * ModelEntity is a joined representation of 'embedded' collections of complex objects representing
 * a {@link Model} proto object. These 'embedded' objects are treated a separate tables which are
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
    @SequenceGenerator(
            name = "model_seq_gen",
            sequenceName = "model_seq",
            allocationSize = 50
    )
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

    public static Model toPojo(ModelEntity modelEntity) {
        return Model.newBuilder()
                .addAllLine(modelEntity.getLines().stream().map(LineEntity::toPojo).toList())
                .addAllTriangle(modelEntity.getTriangles().stream().map(TriangleEntity::toPojo).toList())
                .addAllQuadrilateral(
                        modelEntity.getQuadrilaterals().stream().map(QuadrilateralEntity::toPojo).toList())
                .addAllOptionalLine(
                        modelEntity.getOptionalLines().stream().map(OptionalLineEntity::toPojo).toList())
                .addAllPiece(
                        modelEntity.getChildren().stream().map(ModelEntity::toGpbSubFileReference).toList())
                .build();
    }

    private static SubFileReference toGpbSubFileReference(ModelEntity modelEntity) {
        return SubFileReference.newBuilder()
                .setType(LineType.SUB_FILE_REF)
                .setMatrix(IDENTITY_MATRIX)
                .setSubModel(toPojo(modelEntity))
                .build();
    }

    public @Nullable ModelId getId() {
        return id == null ? null : ModelId.of(id);
    }

    public void setId(ModelId id) {
        this.id = id == null ? null : id.getValue();
    }

}
