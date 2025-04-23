package less.lgeo.parse;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import less.lgeo.geometery.Matrix;
import less.lgeo.primitive.Color;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.LineType;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.SubFileReference;
import less.lgeo.primitive.Triangle;

public class Parser {
  // only UTF-8 encoding
  // file name maximum 255 characters long including extension
  // Special characters, such as &, #, |, and ?, should be avoided as they may also cause cross-platform issues and create problems when used in URLs.
  // Extension
  // All LDraw files carry the LDR (default), DAT or MPD extension.
  // one command per line
  // no line length restriction
  // each command optional leading whitespacce followed by white space delimtied tokens
  // trailing arbitrary data which could include internal white space, treated as a single unit
  // white space is tabs or one or more spacese
  // empty lines or consist of only whitespace are skiped
  // if a line is non-empty the first token must be an integer from the valid line type numbers
  // this number dictates the number of parsing token for that line
  // all lines in the file must usee the line termination of <CR><LF>
  // the file is permitted **but not required** to eend with <CR><LF>

  // LDraw parts are measured in LDraw Units LDU

  private static int toInt(String string) {
    return Integer.parseInt(string);
  }

  // TODO, This should parse into some POJO which we can use to modify or do something with
  public void parse(File toParse) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(
        new FileReader(toParse, StandardCharsets.UTF_8))) {
      bufferedReader.lines().forEachOrdered(this::parseTextLine);
    }
    // TODO setup logger rather than system out
    System.out.println("Finished parsing");
  }

  private void parseTextLine(String line) {
    List<String> values = Arrays.asList(line.trim().split(" +"));

    int commandValue = Integer.parseInt(values.removeFirst());
    LineType lineType = LineType.fromInteger(commandValue);

    switch (lineType) {
      case COMMENT_OR_META_CMD -> parseCommentOrMetaCommand();
      case SUB_FILE_REF -> parseSubFileReference(values);
      case LINE -> parseLine();
      case TRIANGLE -> parseTriangle();
      case QUADRILATERAL -> parseQuadrilateral();
      case OPTIONAL_LINE -> parseOptionalLine();
      default ->
          throw new IllegalStateException("Line Type has an Illegal type of " + lineType.getType());
    }
  }

  private void parseCommentOrMetaCommand() {

  }

  /**
   * Converts a list of strings to a {@link SubFileReference}
   *
   * @param values values to parse
   * @return parsed LDraw {@link SubFileReference}
   */
  private SubFileReference parseSubFileReference(List<String> values) {
    if (values.size() != 14) {
      throw new IllegalArgumentException(
          "Remaining Sub File Reference files does not match format, size is " + values.size());
    }

    Color color = parseColor(values.removeFirst());

    int x = toInt(values.removeFirst());
    int y = toInt(values.removeFirst());
    int z = toInt(values.removeFirst());
    int a = toInt(values.removeFirst());
    int b = toInt(values.removeFirst());
    int c = toInt(values.removeFirst());
    int d = toInt(values.removeFirst());
    int e = toInt(values.removeFirst());
    int f = toInt(values.removeFirst());
    int g = toInt(values.removeFirst());
    int h = toInt(values.removeFirst());
    int i = toInt(values.removeFirst());

    Matrix matrix = new Matrix(x, y, z, a, b, c, d, e, f, g, h, i);
    String fileReference = values.getFirst();

    return new SubFileReference(color, matrix, fileReference);
  }

  private Line parseLine() {
    return null;
  }

  private Triangle parseTriangle() {
    return null;
  }

  private Quadrilateral parseQuadrilateral() {
    return null;
  }

  private Optional<Line> parseOptionalLine() {
    return Optional.empty();
  }

  private Color parseColor(String color) {
    return null;
  }

}
