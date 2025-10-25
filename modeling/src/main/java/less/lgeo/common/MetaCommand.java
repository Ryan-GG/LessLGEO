
package less.lgeo.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetaCommand {
  private final LineType lineType;
  private final String command;
  private final List<String> additionalParams;
}
