package less.lgeo.common;

public record Comment(String value) {
    public static final LineType type = LineType.COMMENT_OR_META_CMD;
}
