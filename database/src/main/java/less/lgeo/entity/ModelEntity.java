package less.lgeo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import less.lgeo.primitive.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Column(unique = false, nullable = false, columnDefinition = "uuid")
  private UUID id;

  @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LineEntity> lines;

  @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TriangleEntity> triangles;

  @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuadrilateralEntity> quadrilaterals;

  @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OptionalLineEntity> optionalLines;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "parent_id")
  private ModelEntity parent;

  @JsonManagedReference
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ModelEntity> children = new ArrayList<>();

  /**
   * Convert a GPB Model to a ModelEntity recursively.
   */
  public static ModelEntity toEntity(Model gpb, ModelEntity parent) {
    UUID modelUUID = UUID.fromString(gpb.getUUID());

    ModelEntity entity = new ModelEntity();
    entity.setId(modelUUID);
    // FIXME
    /*entity.setLines(gpb.getLineList().stream().map(LineEntity::toEntity).toList());
    entity.setTriangles(
        gpb.getTriangleList().stream().map(TriangleEntity::toEntity).toList());
    entity.setQuadrilaterals(
        gpb.getQuadrilateralList().stream().map(QuadrilateralEntity::toEntity).toList());
    entity.setOptionalLines(
        gpb.getOptionalLineList().stream().map(OptionalLineEntity::toEntity).toList());*/
    entity.setParent(parent);

    // Recursively convert children
    List<ModelEntity> childEntities = gpb.getPieceList().stream()
        .map(subModelRef -> toEntity(subModelRef.getSubModel(), entity))
        .toList();
    entity.setChildren(childEntities);

    return entity;
  }

}
