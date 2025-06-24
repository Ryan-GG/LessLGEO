package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import less.lgeo.common.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "color")
public class ColorEntity {

  // TODO, Look for more annotations to include
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

  public static Color toGpb(ColorEntity entity) {
    // TODO, this should be better or figure out a way to more easily convert
    return Color.newBuilder()
        .setId(entity.id)
        .setName(entity.name)
        .setValue(entity.value)
        .setEdge(entity.edge)
        .setAlpha(entity.alpha != null ? entity.alpha : 255)
        .setLuminance(entity.luminance != null ? entity.luminance : 0)
        .setFinish(entity.finish != null ? entity.finish : "")
        .build();
  }

  @Override
  public String toString() {
    return String.format(
        """
            id: %d,
            name: %s,
            value: %s,
            edge: %s,
            alpha: %d,
            luminance: %d,
            finish: %s
            """,
        id, name, value, edge, alpha, luminance, finish);
  }
}
