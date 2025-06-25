package less.lgeo;

import java.io.FileOutputStream;
import java.io.IOException;
import less.lgeo.primitive.Model;
import less.lgeo.producer.ParserProducer;
import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = RabbitProperties.class)
public class ParserHandler {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

  private final ParserProducer parserProducer;
  private final ModelJoiner modelJoiner;

  public ParserHandler(ParserProducer parserProducer, ModelJoiner modelJoiner) {
    this.parserProducer = parserProducer;
    this.modelJoiner = modelJoiner;
  }

  public static void main(String[] args) {
    SpringApplication.run(ParserHandler.class);
  }

  /**
   * See {@link less.lgeo.consumer.ParserConsumer}
   *
   * @param message
   */
  public void consume(String message) {

    Model joinedModel = modelJoiner.joinAndTransformModel(message);

    // TODO, this is for testing not prod, should remove, and 'writeFile'
    writeToFile(joinedModel);

    logger.info("Sending Model...");
    parserProducer.sendMessage(joinedModel);
  }

  private void writeToFile(Model model) {
    try (FileOutputStream output = new FileOutputStream("model.gpb")) {
      model.writeTo(output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}