package less.lgeo.util;

import java.util.List;
import less.lgeo.common.Comment;
import less.lgeo.common.LineType;
import less.lgeo.common.MetaCommand;

public class ParseUtils {

  public static double toDouble( String string ) {
    return Double.parseDouble( string );
  }

  /**
   * @return True, if line is marked as a comment containing '//' as '0 <comment>' format is
   * deprecated
   */
  public static boolean isComment( List<String> values ) {
    return values.getFirst().equals( "//" );
  }

  /**
   * @return If the next string is all Uppercase letters this is treated as a meta command
   * @deprecated
   */
  public static boolean isMetaCommand( List<String> values ) {
    String command = values.getFirst();
    return command.toUpperCase().equals( command );
  }


  /**
   * @return Join line values as singular string 'comment'
   */
  public static Comment parseComment( int lineNumber, List<String> values ) {
    return Comment.newBuilder()
        .setType( LineType.COMMENT_OR_META_CMD )
        .setLineNumber( lineNumber )
        // TODO, Not sure if this needs to be improved
        .setComment( values.toString() )
        .build();
  }

  /**
   * @return MetaCommand, with command and additional parameters
   */
  public static MetaCommand parseCommand( List<String> values ) {
    String command = values.removeFirst();
    return MetaCommand.newBuilder()
        .setType( LineType.COMMENT_OR_META_CMD )
        .setCommand( command )
        // TODO, This needs to actually do something when parsed
        .addAllAdditionalParams( values )
        .build();
  }
}
