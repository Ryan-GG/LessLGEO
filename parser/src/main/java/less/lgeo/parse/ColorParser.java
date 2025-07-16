package less.lgeo.parse;

import static less.lgeo.common.CommonUtils.getLineType;
import static less.lgeo.util.ParseUtils.isMetaCommand;
import static less.lgeo.util.ParseUtils.toInt;

import java.io.BufferedWriter;
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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class ColorParser implements Parser<Color> {

  public static final String COLOR_META_COMMAND = "!COLOUR";
  private static final Logger logger = LoggerFactory.getLogger(ColorParser.class);

  public ColorParser() {
  }

  @Override
  public @Nullable Color parse(String colorMessage) {

    logger.info("Parsing message, {}", colorMessage);

    Iterator<String> iterator = new ArrayList<>(
        List.of(colorMessage.trim().split("\\s+"))).iterator();
    int commandValue = Integer.parseInt(iterator.next());

    if (getLineType(commandValue) != LineType.COMMENT_OR_META_CMD) {
      return null;
    }

    String next = iterator.next();
    if (isMetaCommand(next, COLOR_META_COMMAND)) {
      return buildColor(iterator);
    } else {
      logger.warn("Found different Meta-Command, {}", colorMessage);
    }

    return null;
  }

  @Override
  public void writeToFile(Color gpb, Path outputPath) {
    try (
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))
    ) {

      // Header
      writer.write("id,name,value,edge,alpha,luminance,finish");
      writer.newLine();

      writer.write(String.format("%d,%s,%s,%s,%s,%s,%s",
          gpb.getId(),
          gpb.getName(),
          gpb.getValue(),
          gpb.getEdge(),
          gpb.hasAlpha() ? String.valueOf(gpb.getAlpha()) : "",
          gpb.hasLuminance() ? String.valueOf(gpb.getLuminance()) : "",
          gpb.hasFinish() ? gpb.getFinish() : ""));
      writer.newLine();
    } catch (IOException e) {
      logger.error("Failed to write to {}", outputPath.toAbsolutePath());
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
        default -> {
          String material = builder.hasFinish() ? builder.getFinish() : "";
          String joinedMaterial = material + " " + optionalValueType;
          builder.setFinish(joinedMaterial.trim());
        }
      }
    }
    return builder.build();
  }
}
