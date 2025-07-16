package less.lgeo;


import less.lgeo.common.Color;
import less.lgeo.parse.ColorParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.lang.Nullable;

@SpringBootApplication
public class ColorParserHandler {

  @Autowired
  private final ColorParser colorParser;

  public ColorParserHandler(ColorParser colorParser) {
    this.colorParser = colorParser;
  }

  public static void main(String[] args) {
    SpringApplication.run(ColorParserHandler.class, args);
  }

  /**
   * See {@link less.lgeo.consumer.ColorParserConsumer}
   *
   * @param message
   */
  public @Nullable Color consume(String message) {
    return colorParser.parse(message);
  }
}
