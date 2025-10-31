package less.lgeo.parse;

import less.lgeo.common.*;
import less.lgeo.primitive.*;
import org.joml.Vector3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.*;

@Component
public class LDrawParser implements Parser<Model> {

    private static final Logger logger = LoggerFactory.getLogger(LDrawParser.class);

    private final Map<String, Model> modelCache;

    public LDrawParser() {
        this.modelCache = new ConcurrentHashMap<>();
    }

    @Override
    public void writeToFile(Model gpb, Path outputPath) {
        // TODO, [Task] Add export back to .ldr format of a Model file #24
        // return new File("TODO");
    }

    public Model parse(String toParse) {

        List<MetaCommand> commands = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        List<SubFileReference> pieces = new ArrayList<>();
        List<Line> lines = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();
        List<Quadrilateral> quadrilaterals = new ArrayList<>();
        List<OptionalLine> optionalLines = new ArrayList<>();

        logger.info("Parsing file name: {}", toParse);

        read(toParse).forEach(line -> {
            logger.info("Parsing line, {}", line);

            Iterator<String> lineIterator = List.of(line.split("\\s+")).iterator();

            int commandValue = toInt(lineIterator.next());

            LineType lineType = getLineType(commandValue);

            switch (lineType) {
                case COMMENT_OR_META_CMD -> {
                    if (!lineIterator.hasNext()) {
                        logger.warn("Found '0' line");
                    } else {
                        String command = lineIterator.next();
                        if (isMetaCommand(command)) {
                            commands.add(parseCommand(command, lineIterator));
                        } else {
                            comments.add(parseComment(line));
                        }
                    }
                }
                case SUB_FILE_REF -> pieces.add(parseSubFileReference(lineIterator));
                case LINE -> lines.add(parseLine(lineIterator));
                case TRIANGLE -> triangles.add(parseTriangle(lineIterator));
                case QUADRILATERAL -> quadrilaterals.add(parseQuadrilateral(lineIterator));
                case OPTIONAL_LINE -> optionalLines.add(parseOptionalLine(lineIterator));
                default -> throw new IllegalStateException(
                        "Line Type has an Illegal type of " + lineType);
            }
        });
        logger.info("Finished Parsing");
        return new Model(comments, commands, lines, triangles, quadrilaterals, optionalLines, pieces);
    }


    /**
     * Converts a list of strings to a {@link SubFileReference}
     *
     * @param iterator iterator to parse
     * @return parsed LDraw {@link SubFileReference}
     */
    private SubFileReference parseSubFileReference(Iterator<String> iterator) {
        int colorId = toInt(iterator.next());

        double x = toDouble(iterator.next());
        double y = toDouble(iterator.next());
        double z = toDouble(iterator.next());
        double a = toDouble(iterator.next());
        double b = toDouble(iterator.next());
        double c = toDouble(iterator.next());
        double d = toDouble(iterator.next());
        double e = toDouble(iterator.next());
        double f = toDouble(iterator.next());
        double g = toDouble(iterator.next());
        double h = toDouble(iterator.next());
        double i = toDouble(iterator.next());

        Matrix parsedMatrix = Matrix.builder()
                .x(x)
                .y(y)
                .z(z)
                .a(a)
                .b(b)
                .c(c)
                .d(d)
                .e(e)
                .f(f)
                .g(g)
                .h(h)
                .i(i)
                .scale(1.0)
                .build();



        /*
         * FIXME
         * this recursive parsing is not good, as it will find the first instance of a matching file name,
         * possibly not find the correct directory one. Why there are multiple .dat files with the same name IDK
         */
        List<String> subFileParts = Arrays.stream(iterator.next().split("\\\\")).toList();
        String subFileName = subFileParts.getLast();
        Model parsedSubFileModel = getParsedSubFileModel(subFileName);

        Color color = new Color();
        color.setId(colorId);

        //FIXME??
        return new SubFileReference(
                color,
                parsedMatrix,
                parsedSubFileModel,
                subFileName,
                Optional.empty()
        );
    }

    private @NonNull Model getParsedSubFileModel(String subFileName) {
        // split on \, and use last as file name to search for
        if (this.modelCache.containsKey(subFileName)) {
            return this.modelCache.get(subFileName);
        }

        logger.info("Searching for subFile: {}", subFileName);
        try (Stream<Path> ldrawDir = Files.walk(Path.of("ldraw"))) {

            Optional<Path> subFilePath = ldrawDir.filter(
                            path -> path.getFileName().toString().equals(subFileName))
                    .findFirst();

            if (subFilePath.isEmpty()) {
                throw new IOException();
            }

            String input = Files.readString(subFilePath.get());

            Model parsedSubModel = parse(input);
            this.modelCache.put(subFileName, parsedSubModel);
            return parsedSubModel;

        } catch (IOException ex) {
            logger.error("Sub file does not exist, {}", subFileName);
            throw new IllegalStateException(
                    "Parsed Sub File Model is null, failed trying to parse " + subFileName);
        }
    }

    /**
     * @return parsed LDraw {@link Line}
     */
    private Line parseLine(Iterator<String> iterator) {
        int colorId = toInt(iterator.next());
        double x1 = toDouble(iterator.next());
        double y1 = toDouble(iterator.next());
        double z1 = toDouble(iterator.next());
        double x2 = toDouble(iterator.next());
        double y2 = toDouble(iterator.next());
        double z2 = toDouble(iterator.next());

        Vector3d p1 = new Vector3d(x1, y1, z1);
        Vector3d p2 = new Vector3d(x2, y2, z2);

        Color color = new Color();
        color.setId(colorId);

        return new Line(
                color,
                p1,
                p2
        );
    }

    /**
     * @return parsed LDraw {@link Triangle}
     */
    private Triangle parseTriangle(Iterator<String> iterator) {
        int colorId = toInt(iterator.next());
        double x1 = toDouble(iterator.next());
        double y1 = toDouble(iterator.next());
        double z1 = toDouble(iterator.next());
        double x2 = toDouble(iterator.next());
        double y2 = toDouble(iterator.next());
        double z2 = toDouble(iterator.next());
        double x3 = toDouble(iterator.next());
        double y3 = toDouble(iterator.next());
        double z3 = toDouble(iterator.next());

        Vector3d p1 = new Vector3d(x1, y1, z1);
        Vector3d p2 = new Vector3d(x2, y2, z2);
        Vector3d p3 = new Vector3d(x3, y3, z3);

        Color color = new Color();
        color.setId(colorId);

        return new Triangle(
                color,
                p1,
                p2,
                p3
        );
    }

    /**
     * @return parsed LDraw {@link Quadrilateral}
     */
    private Quadrilateral parseQuadrilateral(Iterator<String> iterator) {
        int colorId = toInt(iterator.next());
        double x1 = toDouble(iterator.next());
        double y1 = toDouble(iterator.next());
        double z1 = toDouble(iterator.next());
        double x2 = toDouble(iterator.next());
        double y2 = toDouble(iterator.next());
        double z2 = toDouble(iterator.next());
        double x3 = toDouble(iterator.next());
        double y3 = toDouble(iterator.next());
        double z3 = toDouble(iterator.next());
        double x4 = toDouble(iterator.next());
        double y4 = toDouble(iterator.next());
        double z4 = toDouble(iterator.next());

        Vector3d p1 = new Vector3d(x1, y1, z1);
        Vector3d p2 = new Vector3d(x2, y2, z2);
        Vector3d p3 = new Vector3d(x3, y3, z3);
        Vector3d p4 = new Vector3d(x4, y4, z4);

        Color color = new Color();
        color.setId(colorId);

        return new Quadrilateral(
                color,
                p1,
                p2,
                p3,
                p4
        );
    }

    /**
     * @return parsed LDraw {@link OptionalLine}
     */
    private OptionalLine parseOptionalLine(Iterator<String> iterator) {
        int colorId = toInt(iterator.next());
        double x1 = toDouble(iterator.next());
        double y1 = toDouble(iterator.next());
        double z1 = toDouble(iterator.next());
        double x2 = toDouble(iterator.next());
        double y2 = toDouble(iterator.next());
        double z2 = toDouble(iterator.next());
        double x3 = toDouble(iterator.next());
        double y3 = toDouble(iterator.next());
        double z3 = toDouble(iterator.next());
        double x4 = toDouble(iterator.next());
        double y4 = toDouble(iterator.next());
        double z4 = toDouble(iterator.next());

        Vector3d p1 = new Vector3d(x1, y1, z1);
        Vector3d p2 = new Vector3d(x2, y2, z2);
        Vector3d p3 = new Vector3d(x3, y3, z3);
        Vector3d p4 = new Vector3d(x4, y4, z4);

        Color color = new Color();
        color.setId(colorId);

        return new OptionalLine(
                color,
                p1,
                p2,
                p3,
                p4
        );
    }

}
