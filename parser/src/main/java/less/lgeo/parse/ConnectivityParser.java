package less.lgeo.parse;

import static less.lgeo.common.CommonUtils.getGroupId;
import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.isMetaCommand;
import static less.lgeo.util.ParseUtils.parseCommand;
import static less.lgeo.util.ParseUtils.parseComment;
import static less.lgeo.util.ParseUtils.toDouble;
import static less.lgeo.util.ParseUtils.toInt;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.connectivity.Connection;
import less.lgeo.connectivity.Connection.Builder;
import less.lgeo.connectivity.GroupId;
import less.lgeo.connectivity.GroupStud;
import less.lgeo.connectivity.PartConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A connectivity Parser will parse a .dat file converted to a .part file and create a respective
 * GPB object from it. This can later be used and joined with the {@link less.lgeo.primitive.Model}
 * GPB object that will be sent to the ReducerHandler.
 */
@Component
public class ConnectivityParser implements Parser<Connection> {

  private static final String PE_CONN_META_CMD = "PE_CONN";
  private static final Logger logger = LoggerFactory.getLogger(ConnectivityParser.class);

  @Override
  public Connection parse(String toParse) {

    Builder connectionBuilder = Connection.newBuilder();

    logger.info("Parsing file name: {}", toParse);

    read(toParse).forEach(line -> {
      logger.info("Parsing line, {}", line);
      Iterator<String> iterator = List.of(line.split("\\s+")).iterator();

      int commandValue = toInt(iterator.next());
      LineType lineType = getLineType(commandValue);

      if (lineType != LineType.COMMENT_OR_META_CMD) {
        throw new IllegalStateException("Unexpected Line Type");
      }

      if (!iterator.hasNext()) {
        logger.warn("Found '0' line");
      } else {
        String command = iterator.next();
        if (isMetaCommand(command)) {
          connectionBuilder.addCommand(parseCommand(command, iterator));
        } else {
          connectionBuilder.addComment(parseComment(line));
        }
      }
    });

    connectionBuilder.build().getCommandList().forEach(command ->
    {
      if (PE_CONN_META_CMD.equals(command.getCommand())) {
        Iterator<String> additionalParamsIter = command.getAdditionalParamsList().iterator();

        GroupId groupId = getGroupId(toInt(additionalParamsIter.next()));

        connectionBuilder.addPartConnection(
            getPartConnection(groupId, additionalParamsIter));

      }
    });
    return connectionBuilder.build();

  }

  private PartConnection getPartConnection(GroupId groupId, Iterator<String> iter) {

    PartConnection.Builder builder = parseBody(groupId, iter);
    return switch (groupId) {
      case GROUP_ZERO -> null;
      case GROUP_ONE -> null;
      case GROUP_STUD -> parseGroupStud(builder, iter);
      case GROUP_FOUR -> null;
      case GROUP_SIX -> null;
      default -> throw new IllegalArgumentException("Unrecognized Group Id");
    };
  }

  private PartConnection.Builder parseBody(GroupId groupId, Iterator<String> iterator) {
    return PartConnection.newBuilder()
        .setGroupId(groupId)
        .setElementId(toInt(iterator.next()))
        .setMatrix(
            Matrix.newBuilder()
                .setA(toDouble(iterator.next()))
                .setB(toDouble(iterator.next()))
                .setC(toDouble(iterator.next()))
                .setD(toDouble(iterator.next()))
                .setE(toDouble(iterator.next()))
                .setF(toDouble(iterator.next()))
                .setG(toDouble(iterator.next()))
                .setH(toDouble(iterator.next()))
                .setI(toDouble(iterator.next()))
                .setX(toDouble(iterator.next()))
                .setY(toDouble(iterator.next()))
                .setZ(toDouble(iterator.next()))
                .setScale(1.0)
        );
  }


  private PartConnection parseGroupStud(PartConnection.Builder builder,
      Iterator<String> iterator) {

    return builder.setGroupStud(
        GroupStud.newBuilder()
            .setZWidthHalfStud(toInt(iterator.next()))
            .setXWidthHalfStud(toInt(iterator.next()))
            .addAllStudGrid(getStudGrid(iterator.next()))
    ).build();
  }

  private List<Boolean> getStudGrid(String studGroupGeometry) {
    String[] studGeometry = studGroupGeometry.split(",");
    return Arrays.stream(studGeometry).map(s ->
    {
      String firstVal = s.split(":")[0];
      return !firstVal.equals("-1") && !firstVal.equals("0");
    }).toList();
  }


  @Override
  public void writeToFile(Connection gpb, Path outputPath) {
    // TODO [Task] Add export back to .ldr format of a Model file #24
  }
}
