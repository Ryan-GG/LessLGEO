package less.lgeo.common;

public record Comment(String comment) {
    private static final LineType lineType = LineType.COMMENT_OR_META_CMD;
}
