package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import less.lgeo.common.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "color")
public class ColorEntity {

  @Id
  @Column(unique = true)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String value;

  @Column(nullable = false)
  private String edge;

  @Column(nullable = true)
  private Integer alpha;

  @Column(nullable = true)
  private Integer luminance;

  @Column(nullable = true)
  private String finish;

  /**
   * @param entity Fields pulled from postgres
   * @return postgres to GPB {@link less.lgeo.common.Color}
   * @throws IllegalArgumentException if entity or required field are null
   */
  public static Color toGpb(@Nullable ColorEntity entity) {

    if (entity == null
        || entity.id == null
        || entity.name == null
        || entity.value == null
        || entity.edge == null
    ) {
      throw new EntityToGpbConversionException(
          String.format("Required ColorEntity field(s) are null, Received: \n%s",
              entity));
    }
    Color.Builder builder = Color.newBuilder()
        .setId(entity.id)
        .setName(entity.name)
        .setValue(entity.value)
        .setEdge(entity.edge);

    if (entity.alpha != null) {
      builder.setAlpha(entity.alpha);
    }
    if (entity.luminance != null) {
      builder.setLuminance(entity.luminance);
    }
    if (entity.finish != null) {
      builder.setFinish(entity.finish);
    }

    return builder.build();
  }

  public static ColorEntity fromGpb(Color toInsert) {
    return new ColorEntity(toInsert.getId(), toInsert.getName(), toInsert.getValue(),
        toInsert.getEdge(), toInsert.getAlpha(), toInsert.getLuminance(), toInsert.getFinish());
  }

  @Override
  public String toString() {
    return null;
  }
}
