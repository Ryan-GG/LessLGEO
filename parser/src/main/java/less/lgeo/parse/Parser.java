package less.lgeo.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import less.lgeo.Matrix;
import less.lgeo.primitive.Color;
import less.lgeo.primitive.Comment;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.LineType;
import less.lgeo.primitive.MetaCommand;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;
import less.lgeo.set.Model;
import less.lgeo.set.Model.Builder;

public class Parser {

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

  private static double toDouble(String string) {
    return Double.parseDouble(string);
  }

  public Model parse(File toParse) throws IOException {
    Model.Builder modelBuilder = Builder.newBuilder();
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(toParse, StandardCharsets.UTF_8))) {
      AtomicInteger lineNumber = new AtomicInteger();
      bufferedReader.lines().forEach(line -> {
        List<String> values = new ArrayList<>(List.of(line.trim().split(" ")));

        double commandValue = Integer.parseInt(values.removeFirst());
        LineType lineType = LineType.fromInteger(commandValue);

        switch (lineType) {
          case COMMENT_OR_META_CMD -> {
            if (isMetaCommand(values)) {
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
              "Line Type has an Illegal type of " + lineType.getType());
        }
        lineNumber.getAndIncrement();
      });
      // TODO setup logger rather than system out
      System.out.println("Finished parsing");
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
    return values.getFirst().toUpperCase().equals(values.getFirst());
  }

  /**
   * @return Join line values as singular string 'comment'
   */
  private Comment parseComment(int lineNumber, List<String> values) {
    return new Comment(lineNumber, values.toString());
  }

  /**
   * @return MetaCommand, with command and additional parameters
   */
  private MetaCommand parseCommand(List<String> values) {
    String command = values.removeFirst();
    return new MetaCommand(command, values);
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

    Matrix matrix = new Matrix(x, y, z, a, b, c, d, e, f, g, h, i);
    Path fileReference = new File(values.getFirst()).toPath();

    return new SubFileReference(color, matrix, fileReference);
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
    return new Line(color, x1, y1, z1, x2, y2, z2);
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
    return new Triangle(color, x1, y1, z1, x2, y2, z2, x3, y3, z3);
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

    return new Quadrilateral(color, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
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

    return new OptionalLine(color, x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
  }

  /**
   * @return parsed LDraw {@link Color}
   */
  private Color parseColor(String color) {
    return null;
  }

}
