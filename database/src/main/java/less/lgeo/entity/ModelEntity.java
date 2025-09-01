package less.lgeo.entity;

import com.google.protobuf.InvalidProtocolBufferException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.primitive.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ModelEntity represents queryable fields in a {@link Model} proto object as well as the binary
 * data( byte[] ) that makes up the entire object
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "models")
public class ModelEntity {

  @Id
  @Column(unique = true, nullable = false, columnDefinition = "uuid")
  private UUID uuid;

  @Column(nullable = false, columnDefinition = "bytea")
  private byte[] modelData;

  @ElementCollection
  @CollectionTable(
      name = "model_vertices",
      joinColumns = @JoinColumn(
          name = "model_uuid",
          referencedColumnName = "uuid",
          unique = true,
          nullable = false,
          columnDefinition = "uuid",
          table = "models"
      )
  )
  private List<VertexEmbeddable> vertices;

  public static ModelEntity toEntity(Model gpb) {
    UUID modelUUID = UUID.fromString(gpb.getUUID());
    return new ModelEntity(modelUUID, gpb.toByteArray(), List.of(new VertexEmbeddable(0, 0, 0)));
  }

  public static Model toGpb(ModelEntity modelEntity) throws InvalidProtocolBufferException {
    return Model.parseFrom(modelEntity.getModelData());
  }
}
