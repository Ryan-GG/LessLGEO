package less.lgeo.parse;

import less.lgeo.common.Comment;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.MetaCommand;
import less.lgeo.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.*;

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

        List<MetaCommand> commands = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<Connection.PartConnection> connections = new ArrayList<>();

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
                    commands.add(parseCommand(command, iterator));
                } else {
                    comments.add(parseComment(line));
                }
            }
        });

        commands.forEach(command ->
        {
            if (PE_CONN_META_CMD.equals(command.command())) {
                Iterator<String> additionalParamsIter = command.additionalParams().iterator();

                //FIXME, I'm not sure what values additonal params list corresponds too
                Connection.GroupId groupId = Connection.GroupId.fromValue(toInt(additionalParamsIter.next()));

                connections.add(getPartConnection(groupId, additionalParamsIter));

            }
        });
        return new Connection(comments, commands, connections);

    }

    private Connection.PartConnection getPartConnection(Connection.GroupId groupId, Iterator<String> iter) {

        Connection.PartConnection.PartConnectionBuilder builder = parseBody(groupId, iter);
        return switch (groupId) {
            // FIXME, don't know what do for these connection groups yet
            case GROUP_ZERO, GROUP_ONE, GROUP_FOUR, GROUP_SIX -> builder.build();
            case GROUP_STUD -> parseGroupStud(builder, iter);
            default -> throw new IllegalArgumentException("Unrecognized Group Id");
        };
    }

    private Connection.PartConnection.PartConnectionBuilder parseBody(Connection.GroupId groupId, Iterator<String> iterator) {
        return Connection.PartConnection.builder()
                .groupId(groupId)
                .elementId(toInt(iterator.next()))
                .matrix(
                        new Matrix(
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                toDouble(iterator.next()),
                                1.0)
                );
    }


    private Connection.PartConnection parseGroupStud(Connection.PartConnection.PartConnectionBuilder builder,
                                                     Iterator<String> iterator) {

        return builder.groupStud(
                new Connection.GroupStud(
                        toInt(iterator.next()),
                        toInt(iterator.next()),
                        getStudGrid(iterator.next())
                )).build();
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
