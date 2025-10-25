package less.lgeo.parse;

import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.common.Vector3Utils.toVector3;
import static less.lgeo.primitive.LineUtils.toLine;
import static less.lgeo.primitive.OptionalLineUtils.toOptionalLine;
import static less.lgeo.primitive.QuadrilateralUtils.toQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.toTriangle;
import static less.lgeo.util.ParseUtils.isMetaCommand;
import static less.lgeo.util.ParseUtils.parseCommand;
import static less.lgeo.util.ParseUtils.parseComment;
import static less.lgeo.util.ParseUtils.toDouble;
import static less.lgeo.util.ParseUtils.toInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import less.lgeo.common.LineType;
import less.lgeo.common.Matrix;
import less.lgeo.common.Vector3;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

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

    Model.Builder modelBuilder = Model.newBuilder();

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
            "Line Type has an Illegal type of " + lineType.getDescriptorForType().toString());
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

    Matrix parsedMatrix = Matrix.newBuilder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .setA(a)
        .setB(b)
        .setC(c)
        .setD(d)
        .setE(e)
        .setF(f)
        .setG(g)
        .setH(h)
        .setI(i)
        .setScale(1.0)
        .build();



    /*
     * FIXME
     * this recursive parsing is not good, as it will find the first instance of a matching file name,
     * possibly not find the correct directory one. Why there are multiple .dat files with the same name IDK
     */
    List<String> subFileParts = Arrays.stream(iterator.next().split("\\\\")).toList();
    String subFileName = subFileParts.getLast();
    Model parsedSubFileModel = getParsedSubFileModel(subFileName);
    return SubFileReference.newBuilder()
        .setFileName(subFileName)
        .setType(LineType.SUB_FILE_REF)
        .setColorId(colorId)
        .setMatrix(parsedMatrix)
        .setSubModel(parsedSubFileModel)
        .build();
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

    Vector3 p1 = toVector3(x1, y1, z1);
    Vector3 p2 = toVector3(x2, y2, z2);
    return toLine(
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

    Vector3 p1 = toVector3(x1, y1, z1);
    Vector3 p2 = toVector3(x2, y2, z2);
    Vector3 p3 = toVector3(x3, y3, z3);
    return toTriangle(
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

    Vector3 p1 = toVector3(x1, y1, z1);
    Vector3 p2 = toVector3(x2, y2, z2);
    Vector3 p3 = toVector3(x3, y3, z3);
    Vector3 p4 = toVector3(x4, y4, z4);
    return toQuadrilateral(
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

    Vector3 p1 = toVector3(x1, y1, z1);
    Vector3 p2 = toVector3(x2, y2, z2);
    Vector3 p3 = toVector3(x3, y3, z3);
    Vector3 p4 = toVector3(x4, y4, z4);
    return toOptionalLine(
        colorId,
        p1,
        p2,
        p3,
        p4
    );
  }

}
