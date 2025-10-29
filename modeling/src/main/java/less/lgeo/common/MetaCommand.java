package less.lgeo.common;

import java.util.List;

public record MetaCommand(String command, List<String> additionalParams) {
    private static final LineType lineType = LineType.COMMENT_OR_META_CMD;
}
