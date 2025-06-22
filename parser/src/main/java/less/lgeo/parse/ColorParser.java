package less.lgeo.parse;

import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.isMetaCommand;
import static less.lgeo.util.ParseUtils.toInt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import less.lgeo.common.Color;
import less.lgeo.common.LineType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ColorParser implements Parser<List<Color>> {

  public static final String COLOR_META_COMMAND = "!COLOUR";
  private static final Logger logger = LoggerFactory.getLogger(ColorParser.class);

  public ColorParser() {
  }

  public static void main(String[] args) throws IOException {
    File file = new File(args[0]);

    List<Color> colors = new ColorParser().parse(file);
    new ColorParser().writeToFile(colors, Path.of("colors.csv"));
  }

  @Override
  public List<Color> parse(File file) throws IOException {

    List<Color> colors = new ArrayList<>();

    logger.info("Parsing file name: {}", file);

    read(file).forEach(line -> {
      logger.info("Parsing line, {}", line);

      logger.info(List.of(line.trim().split("\\s+")).toString());

      Iterator<String> iterator = new ArrayList<>(List.of(line.trim().split("\\s+"))).iterator();
      int commandValue = Integer.parseInt(iterator.next());

      LineType lineType = getLineType(commandValue);

      switch (lineType) {
        case COMMENT_OR_META_CMD -> {
          if (!iterator.hasNext()) {
            logger.warn("Found '0' line");
          } else {
            String next = iterator.next();
            if (isMetaCommand(next, COLOR_META_COMMAND)) {
              colors.add(buildColor(iterator));
            } else {
              logger.info("Found different Meta-Command, {}", line);
            }
          }
        }
        default -> logger.info("Skipping {}", line);
      }
    });

    logger.info("Parsed Colors {}", colors);
    return colors;
  }

  @Override
  public void writeToFile(List<Color> gpb, Path outputPath) {
    try (
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))
    ) {

      // Header
      writer.write("id,name,value,edge,alpha,luminance,finish");
      writer.newLine();

      for (Color color : gpb) {
        writer.write(String.format("%d,%s,%s,%s,%s,%s,%s",
            color.getId(),
            color.getName(),
            color.getValue(),
            color.getEdge(),
            color.hasAlpha() ? String.valueOf(color.getAlpha()) : "",
            color.hasLuminance() ? String.valueOf(color.getLuminance()) : "",
            color.hasFinish() ? color.getFinish() : ""));
        writer.newLine();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Color buildColor(Iterator<String> colorIterator) {

    Color.Builder builder = Color.newBuilder();

    builder.setName(colorIterator.next());

    String fieldName = colorIterator.next();
    if (!("CODE".equals(fieldName))) {
      throw new IllegalStateException("No Color Id, Received: " + fieldName);
    }
    int id = toInt(colorIterator.next());
    builder.setId(id);

    fieldName = colorIterator.next();
    if (!("VALUE".equals(fieldName))) {
      throw new IllegalStateException("No Color Value, Received: " + fieldName);
    }
    String value = colorIterator.next();
    builder.setValue(value);

    fieldName = colorIterator.next();
    if (!("EDGE".equals(fieldName))) {
      throw new IllegalStateException("No Color Value, Edge: " + fieldName);
    }
    String edge = colorIterator.next();
    builder.setEdge(edge);

    while (colorIterator.hasNext()) {
      String optionalValueType = colorIterator.next();
      switch (optionalValueType) {
        case "ALPHA" -> builder.setAlpha(toInt(colorIterator.next()));
        case "LUMINANCE" -> builder.setLuminance(toInt(colorIterator.next()));
        default -> builder.setFinish(optionalValueType);
      }
    }
    return builder.build();
  }
}
