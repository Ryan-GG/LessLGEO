package less.lgeo.common;

public enum LineType {
    
    LINE_TYPE_UNKNOWN,
    COMMENT_OR_META_CMD,
    SUB_FILE_REF,
    LINE,
    TRIANGLE,
    QUADRILATERAL,
    OPTIONAL_LINE;

    public static LineType getLineType(int commandValue) {
        switch (commandValue) {
            case 0 -> {
                return LineType.COMMENT_OR_META_CMD;
            }
            case 1 -> {
                return LineType.SUB_FILE_REF;
            }
            case 2 -> {
                return LineType.LINE;
            }
            case 3 -> {
                return LineType.TRIANGLE;
            }
            case 4 -> {
                return LineType.QUADRILATERAL;
            }
            case 5 -> {
                return LineType.OPTIONAL_LINE;
            }
            default -> {
                return LineType.LINE_TYPE_UNKNOWN;
            }
        }
    }
}
