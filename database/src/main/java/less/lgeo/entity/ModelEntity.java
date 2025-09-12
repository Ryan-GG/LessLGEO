package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import less.lgeo.primitive.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * ModelEntity is a joined representation of 'embedded' collections of complex objects representing
 * a {@link Model} proto object. These 'embedded' objects are treated a separate tables which are
 * joined by the model uuid
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "models")
public class ModelEntity {

    @Id
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<LineEntity> lines;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TriangleEntity> triangles;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<QuadrilateralEntity> quadrilaterals;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OptionalLineEntity> optionalLines;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_id")
    private ModelEntity parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<ModelEntity> children = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
