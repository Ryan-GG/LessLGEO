package less.lgeo.util;

import java.util.Iterator;
import java.util.List;
import less.lgeo.common.Comment;
import less.lgeo.common.LineType;
import less.lgeo.common.MetaCommand;

public class ParseUtils {

  public static double toDouble(String string) {
    return Double.parseDouble(string);
  }

  public static int toInt(String string) {
    return Integer.parseInt(string);
  }


  /**
   * @return True, if line is marked as a comment containing '//' as '0 <comment>' format is
   * deprecated
   */
  public static boolean isComment(List<String> values) {
    return values.getFirst().equals("//");
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


  /**
   * @return Join line values as singular string 'comment'
   */
  public static Comment parseComment(String commentLine) {
    return Comment.newBuilder()
        .setType(LineType.COMMENT_OR_META_CMD)
        .setComment(commentLine)
        .build();
  }

  /**
   * @return MetaCommand, with command and additional parameters
   */
  public static MetaCommand parseCommand(String command, Iterator<String> iterator) {

    MetaCommand.Builder builder = MetaCommand.newBuilder()
        .setType(LineType.COMMENT_OR_META_CMD)
        .setCommand(command);

    iterator.forEachRemaining(builder::addAdditionalParams);

    return builder.build();
  }
}
