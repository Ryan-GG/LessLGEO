package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import less.lgeo.primitive.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "models" )
public class ModelEntity {

  @Id
  @Column( unique = true, nullable = false )
  private String uuid;

  @Column( nullable = false, columnDefinition = "bytea" )
  private byte[] modelData;

  public static ModelEntity toEntity( Model gpb ) {
    return new ModelEntity( gpb.getUUID(), gpb.toByteArray() );
  }
}
