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

  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<LineEntity> lines;

  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TriangleEntity> triangles;

  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuadrilateralEntity> quadrilaterals;

  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OptionalLineEntity> optionalLines;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "parent_id")
  private ModelEntity parent;

  @JsonManagedReference
  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ModelEntity> children = new ArrayList<>();

}
