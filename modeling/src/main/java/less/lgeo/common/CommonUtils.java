package less.lgeo.common;

import java.util.Optional;

public class CommonUtils {

    public static String COL_EXT = ".col";
    public static String DAT_EXT = ".dat";
    // Part Extension defines connections via PE_CONN meta command
    // This is because traditional .conn files are proprietary and cannot be parsed
    // normally
    public static String PART_EXT = ".part";

    /**
     * TODO: Come back and verify if this is need or the gpb can be modified
     *
     * <p>
     * This weird switch statement is because GPB wants the unknown value as 0,
     * cause that's the
     * default value assigned to the empty gpb
     * </p>
     *
     * @param commandValue Line's Command Value
     * @return Returns the corrected LineType Enum based on the scene line value
     */
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

    // public static GroupId getGroupId(int value) {
    // return switch (value) {
    // case 0 -> GROUP_ZERO;
    // case 1 -> GROUP_ONE;
    // case 2, 3 -> GROUP_STUD;
    // case 4 -> GROUP_FOUR;
    // case 6 -> GROUP_SIX;
    // default -> UNRECOGNIZED;
    // };
    // }

    public static String changeFileExtension(String subFileName, String extension) {
        String fileName = subFileName.substring(0, subFileName.lastIndexOf("."));
        return fileName.concat(extension);
    }

    /**
     * @param inheritedColor Possibly inherit the color based on the
     *                       subPartColorId
     * @param subPartColor   subPart's color dictates if inheritedColor is needed
     *                       based on reserved
     *                       color codes 16,24
     * @return Color GPB
     */
    public static Color getColor(Optional<Color> inheritedColor, Color subPartColor) {

        return inheritedColor.map(color -> switch (subPartColor.getId()) {
                    case 16, 24 -> color;
                    default -> subPartColor;
                })
                .orElse(subPartColor);

    }
}
