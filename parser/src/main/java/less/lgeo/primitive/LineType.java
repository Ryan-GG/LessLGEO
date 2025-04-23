package less.lgeo.primitive;

public enum LineType {
    COMMENT_OR_META_CMD(0),
    SUB_FILE_REF(1),
    LINE(2),
    TRIANGLE(3),
    QUADRILATERAL(3),
    OPTIONAL_LINE(4),
    INVALID(5);

    private final int type;

    LineType(int type)
    {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    // Static method to convert int to enum
    public static LineType fromInteger(int ordinalValue) {
        for (LineType type : LineType.values()) {
            if (type.getType() == ordinalValue) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Status code: " + ordinalValue );
    }

    @Override
    public String toString() {
        return this.name() + " (" + this.type + ")";
    }
}
