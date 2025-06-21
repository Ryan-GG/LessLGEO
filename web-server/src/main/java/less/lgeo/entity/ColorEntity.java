package less.lgeo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "color")
public class ColorEntity {

  // TODO, Look for more annotations to include
  @Id
  @Column(unique = true)
  private Integer code;

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

}
