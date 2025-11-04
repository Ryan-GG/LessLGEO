package less.lgeo.common;

import java.util.List;

public record Comment(String value) {
    public static final LineType type = LineType.COMMENT_OR_META_CMD;

    /**
     * @return True, if line is marked as a value containing '//' as '0 <value>' format is
     * deprecated
     */
    public static boolean isComment(List<String> values) {
        return values.getFirst().equals("//");
    }
}
