package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import less.lgeo.common.Color;
import less.lgeo.exception.EntityToGpbConversionException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colors")
public class ColorEntity {

  @Id
  @Column(unique = true, nullable = false)
  private int id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String rgb;

  @Column(nullable = false)
  private boolean isTrans;

  @Column(nullable = false)
  private int numParts;

  @Column(nullable = false)
  private int numSets;

  @Column(name = "y1")
  private Integer startYear;

  @Column(name = "y2")
  private Integer endYear;

  /**
   * @param entity Fields pulled from postgres
   * @return postgres to GPB {@link less.lgeo.common.Color}
   * @throws IllegalArgumentException if entity or required field are null
   */
  public static @NonNull Color toGpb(@Nullable ColorEntity entity)
      throws EntityToGpbConversionException {
    if (entity == null) {
      throw new EntityToGpbConversionException("ColorEntity was null");
    }
    return Color.newBuilder()
        .setId(entity.id)
        .setName(entity.name)
        .setRgb(entity.rgb)
        .setIsTrans(entity.isTrans)
        .build();
  }

  @Override
  public String toString() {
    return null;
  }
}
