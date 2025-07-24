package less.lgeo.entity;

import com.google.protobuf.InvalidProtocolBufferException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
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

  public static ModelEntity toEntity(Model gpb) {
    UUID modelUUID = UUID.fromString(gpb.getUUID());
    return new ModelEntity(modelUUID, gpb.toByteArray());
  }

  public static Model toGpb(ModelEntity modelEntity) throws InvalidProtocolBufferException {
    return Model.parseFrom(modelEntity.getModelData());
  }
}
