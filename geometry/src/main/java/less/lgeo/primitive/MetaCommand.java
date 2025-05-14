package less.lgeo.primitive;

import java.util.List;

public class MetaCommand {

  //TODO, this should be a database table or something when new commands come in
  private final String command;

  private final List<String> additionalParams;

  public MetaCommand(String command, List<String> additionalParams) {
    this.command = command;
    this.additionalParams = additionalParams;
  }

  @Override
  public String toString() {
    return String.format("""
            {
              command='%s',
              additionalParams='%s'
            }
            """,
        this.command,
        this.additionalParams
    );
  }
}
