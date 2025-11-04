package less.lgeo.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public record MetaCommand(String command, List<String> additionalParams) {
    public static final LineType type = LineType.COMMENT_OR_META_CMD;

    /**
     * @return MetaCommand, with command and additional parameters
     */
    public static MetaCommand parseCommand(String command, Iterator<String> iterator) {

        List<String> additionalParams = new ArrayList<>();
        iterator.forEachRemaining(additionalParams::add);

        return new MetaCommand(command, additionalParams);

    }

    /**
     * @return If the next string is all Uppercase letters this is treated as a meta command
     */
    public static boolean isMetaCommand(String metaCommand) {
        String copyCommand = String.copyValueOf(metaCommand.toCharArray());
        return copyCommand.toUpperCase().equals(metaCommand);
    }

    /**
     * Meta commands should start with ! and end with expected text
     *
     * @param actual   Parsed input
     * @param expected expected command
     * @return true, if input == expected, false otherwise
     */
    public static boolean isMetaCommand(String actual, String expected) {
        return actual.startsWith("!") && actual.endsWith(expected);
    }
}
