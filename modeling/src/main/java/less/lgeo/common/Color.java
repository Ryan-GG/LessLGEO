package less.lgeo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Color {

  private final long id;
  private final String name;
  private final String rgb;
  private final boolean isTransparent;

}
