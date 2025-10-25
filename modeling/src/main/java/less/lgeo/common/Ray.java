package less.lgeo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ray {
  private final Vector3 origin;
  private final Vector3 direction;
  private final double scale;
}
