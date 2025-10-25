
package less.lgeo.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Comment {
  private final LineType lineType;
  private final String comment;
}
