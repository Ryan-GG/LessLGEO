package less.lgeo.parser;

import static less.lgeo.primitive.LineUtils.getLine;
import static less.lgeo.primitive.OptionalLineUtils.getOptionalLine;
import static less.lgeo.primitive.PrimitiveUtils.getLineType;
import static less.lgeo.primitive.PrimitiveUtils.getPoint;
import static less.lgeo.primitive.QuaderilateralUtils.getQuadrilateral;
import static less.lgeo.primitive.TriangleUtils.getTriangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import less.lgeo.primitive.Color;
import less.lgeo.primitive.Comment;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.LineType;
import less.lgeo.primitive.MetaCommand;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Parser {

  private static final Logger logger = LoggerFactory.getLogger(Parser.class);
  // only UTF-8 encoding
  // file name maximum 255 characters long including extension
  // Special characters, such as &, #, |, and ?, should be avoided as they may
  // also cause cross-platform issues and create problems when used in URLs.
  // Extension
  // All LDraw files carry the LDR (default), DAT or MPD extension.
  // one command per line
  // no line length restriction
  // each command optional leading whitespacce followed by white space delimtied
  // tokens
  // trailing arbitrary data which could include internal white space, treated as
  // a single unit
  // white space is tabs or one or more spacese
  // empty lines or consist of only whitespace are skiped
  // if a line is non-empty the first token must be an integer from the valid line
  // type numbers
  // this number dictates the number of parsing token for that line
  // all lines in the file must usee the line termination of <CR><LF>
  // the file is permitted **but not required** to eend with <CR><LF>

  // LDraw parts are measured in LDraw Units LDU

  public Parser() {

  }

  private static double toDouble(String string) {
    return Double.parseDouble(string);
  }

  public Model parse(File toParse) throws IOException {
    Model.Builder modelBuilder = Model.newBuilder();
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(toParse, StandardCharsets.UTF_8))) {

      logger.info("Parsing file name: {}", toParse);
      AtomicInteger lineNumber = new AtomicInteger();

      bufferedReader.lines().filter(StringUtils::hasText).forEach(line -> {
        List<String> values = new ArrayList<>(List.of(line.trim().split(" ")));
        int commandValue = Integer.parseInt(values.removeFirst());

        LineType lineType = getLineType(commandValue);

        switch (lineType) {
          case COMMENT_OR_META_CMD -> {
            if (values.isEmpty()) {
              logger.warn("Found '0' line");
            } else if (isMetaCommand(values)) {
              modelBuilder.addCommand(parseCommand(values));
            } else {
              modelBuilder.addComment(parseComment(lineNumber.get(), values));
            }
          }
          case SUB_FILE_REF -> modelBuilder.addPiece(parseSubFileReference(values));
          case LINE -> modelBuilder.addLine(parseLine(values));
          case TRIANGLE -> modelBuilder.addTriangle(parseTriangle(values));
          case QUADRILATERAL -> modelBuilder.addQuadrilateral(parseQuadrilateral(values));
          case OPTIONAL_LINE -> modelBuilder.addOptionalLine(parseOptionalLine(values));
          default -> throw new IllegalStateException(
              "Line Type has an Illegal type of " + lineType.getDescriptorForType().toString());
        }
        lineNumber.getAndIncrement();
      });
      logger.info("Finished Parsing");
      return modelBuilder.build();
    }
  }

  /**
   * @return True, if line is marked as a comment containing '//' as '0 <comment>' format is
   * deprecated
   */
  private boolean isComment(List<String> values) {
    return values.getFirst().equals("//");
  }

  /**
   * @return If the next string is all Uppercase letters this is treated as a meta command
   * @deprecated
   */
  private boolean isMetaCommand(List<String> values) {
    String command = values.getFirst();
    return command.toUpperCase().equals(command);
  }


  /**
   * @return Join line values as singular string 'comment'
   */
  private Comment parseComment(int lineNumber, List<String> values) {
    return Comment.newBuilder()
        .setType(LineType.COMMENT_OR_META_CMD)
        .setLineNumber(lineNumber)
        // TODO, Not sure if this needs to be improved
        .setComment(values.toString())
        .build();
  }

  /**
   * @return MetaCommand, with command and additional parameters
   */
  private MetaCommand parseCommand(List<String> values) {
    String command = values.removeFirst();
    return MetaCommand.newBuilder()
        .setType(LineType.COMMENT_OR_META_CMD)
        .setCommand(command)
        // TODO, This needs to actually do something when parsed
        .addAllAdditionalParams(values)
        .build();
  }

  /**
   * Converts a list of strings to a {@link SubFileReference}
   *
   * @param values values to parse
   * @return parsed LDraw {@link SubFileReference}
   */
  private SubFileReference parseSubFileReference(List<String> values) {
    if (values.size() != 14) {
      throw new IllegalStateException(
          "Remaining Sub File Reference files does not match format, size is " + values.size());
    }

    Color color = parseColor(values.removeFirst());

    double x = toDouble(values.removeFirst());
    double y = toDouble(values.removeFirst());
    double z = toDouble(values.removeFirst());
    double a = toDouble(values.removeFirst());
    double b = toDouble(values.removeFirst());
    double c = toDouble(values.removeFirst());
    double d = toDouble(values.removeFirst());
    double e = toDouble(values.removeFirst());
    double f = toDouble(values.removeFirst());
    double g = toDouble(values.removeFirst());
    double h = toDouble(values.removeFirst());
    double i = toDouble(values.removeFirst());

    /*
     * FIXME
     * this recursive parsing is not good, as it will find the first instance of a matching file name,
     * possibly not find the correct directory one. Why there are multiple .dat files with the same name IDK
     */
    List<String> subFileParts = Arrays.stream(values.getFirst().split("\\\\")).toList();
    String subFileName = subFileParts.getLast();
    Model parsedSubModel = null;
    // split on \, and use last as file name to search for
    try (Stream<Path> ldrawDir = Files.walk(Path.of("ldraw"))) {

      Optional<Path> subFilePath = ldrawDir.filter(
              path -> path.getFileName().toString().equals(subFileName))
          .findFirst();
      if (subFilePath.isEmpty()) {
        throw new IOException();
      }

      parsedSubModel = parse(subFilePath.get().toFile());

    } catch (IOException ex) {
      logger.error("Sub file does not exist, {}", subFileName);
    }
    if (parsedSubModel == null) {
      throw new IllegalStateException("Sub File Reference is null");
    }
    return SubFileReference.newBuilder()
        .setType(LineType.SUB_FILE_REF)
        .setColor(
            Color.getDefaultInstance()
        )
        .setMatrix(
            less.lgeo.primitive.Matrix.newBuilder()
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
                .build()
        )
        .setSubModel(parsedSubModel)
        .build();
  }

  /**
   * @return parsed LDraw {@link Line}
   */
  private Line parseLine(List<String> values) {
    if (values.size() != 7) {
      throw new IllegalStateException(
          "Remaining Line does not match format, size is " + values.size());
    }
    Color color = parseColor(values.removeFirst());
    double x1 = toDouble(values.removeFirst());
    double y1 = toDouble(values.removeFirst());
    double z1 = toDouble(values.removeFirst());
    double x2 = toDouble(values.removeFirst());
    double y2 = toDouble(values.removeFirst());
    double z2 = toDouble(values.removeFirst());
    return getLine(
        color,
        getPoint(x1, y1, z1),
        getPoint(x2, y2, z2)
    );
  }

  /**
   * @return parsed LDraw {@link Triangle}
   */
  private Triangle parseTriangle(List<String> values) {
    if (values.size() != 10) {
      throw new IllegalStateException(
          "Remaining Triangle does not match format, size is " + values.size());
    }
    Color color = parseColor(values.removeFirst());
    double x1 = toDouble(values.removeFirst());
    double y1 = toDouble(values.removeFirst());
    double z1 = toDouble(values.removeFirst());
    double x2 = toDouble(values.removeFirst());
    double y2 = toDouble(values.removeFirst());
    double z2 = toDouble(values.removeFirst());
    double x3 = toDouble(values.removeFirst());
    double y3 = toDouble(values.removeFirst());
    double z3 = toDouble(values.removeFirst());
    return getTriangle(
        color,
        getPoint(x1, y1, z1),
        getPoint(x2, y2, z2),
        getPoint(x3, y3, z3)
    );
  }

  /**
   * @return parsed LDraw {@link Quadrilateral}
   */
  private Quadrilateral parseQuadrilateral(List<String> values) {
    if (values.size() != 13) {
      throw new IllegalStateException(
          "Remaining Quadrilateral does not match format, size is " + values.size());
    }
    Color color = parseColor(values.removeFirst());
    double x1 = toDouble(values.removeFirst());
    double y1 = toDouble(values.removeFirst());
    double z1 = toDouble(values.removeFirst());
    double x2 = toDouble(values.removeFirst());
    double y2 = toDouble(values.removeFirst());
    double z2 = toDouble(values.removeFirst());
    double x3 = toDouble(values.removeFirst());
    double y3 = toDouble(values.removeFirst());
    double z3 = toDouble(values.removeFirst());
    double x4 = toDouble(values.removeFirst());
    double y4 = toDouble(values.removeFirst());
    double z4 = toDouble(values.removeFirst());

    return getQuadrilateral(
        color,
        getPoint(x1, y1, z1),
        getPoint(x2, y2, z2),
        getPoint(x3, y3, z3),
        getPoint(x4, y4, z4)
    );
  }

  /**
   * @return parsed LDraw {@link OptionalLine}
   */
  private OptionalLine parseOptionalLine(List<String> values) {
    if (values.size() != 13) {
      throw new IllegalStateException(
          "Remaining Optional Line does not match format, size is " + values.size());
    }
    Color color = parseColor(values.removeFirst());
    double x1 = toDouble(values.removeFirst());
    double y1 = toDouble(values.removeFirst());
    double z1 = toDouble(values.removeFirst());
    double x2 = toDouble(values.removeFirst());
    double y2 = toDouble(values.removeFirst());
    double z2 = toDouble(values.removeFirst());
    double x3 = toDouble(values.removeFirst());
    double y3 = toDouble(values.removeFirst());
    double z3 = toDouble(values.removeFirst());
    double x4 = toDouble(values.removeFirst());
    double y4 = toDouble(values.removeFirst());
    double z4 = toDouble(values.removeFirst());

    return getOptionalLine(
        color,
        getPoint(x1, y1, z1),
        getPoint(x2, y2, z2),
        getPoint(x3, y3, z3),
        getPoint(x4, y4, z4)
    );
  }

  /**
   * @return parsed LDraw {@link Color}
   */
  private Color parseColor(String color) {
    return Color.getDefaultInstance();
  }

}
