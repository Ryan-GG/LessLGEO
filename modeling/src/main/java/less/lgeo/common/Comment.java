package less.lgeo.common;

public record Comment(String value) {
    public static final LineType lineType = LineType.COMMENT_OR_META_CMD;
}
