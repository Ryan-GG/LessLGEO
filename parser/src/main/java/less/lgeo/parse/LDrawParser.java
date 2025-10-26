package less.lgeo.parse;

import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
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

        Model.ModelBuilder modelBuilder = Model.builder();

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
                            modelBuilder.addCommand(parseCommand(command, lineIterator));
                        } else {
                            modelBuilder.addComment(parseComment(line));
                        }
                    }
                }
                case SUB_FILE_REF -> modelBuilder.addPiece(parseSubFileReference(lineIterator));
                case LINE -> modelBuilder.addLine(parseLine(lineIterator));
                case TRIANGLE -> modelBuilder.addTriangle(parseTriangle(lineIterator));
                case QUADRILATERAL -> modelBuilder.addQuadrilateral(parseQuadrilateral(lineIterator));
                case OPTIONAL_LINE -> modelBuilder.addOptionalLine(parseOptionalLine(lineIterator));
                default -> throw new IllegalStateException(
                        "Line Type has an Illegal type of " + lineType);
            }
        });
        logger.info("Finished Parsing");
        return modelBuilder.build();
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

        return new SubFileReference(
                colorId,
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
        return new Line(
                colorId,
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
        return new Triangle(
                colorId,
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
        return new Quadrilateral(
                colorId,
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
        return new OptionalLine(
                colorId,
                p1,
                p2,
                p3,
                p4
        );
    }

}
