package less.lgeo;

import less.lgeo.primitive.Model;
import less.lgeo.producer.ParserProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserHandler {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

  @Autowired
  private final ParserProducer parserProducer;

  @Autowired
  private final ModelJoiner modelJoiner;

  public ParserHandler(ParserProducer parserProducer, ModelJoiner modelJoiner) {
    this.parserProducer = parserProducer;
    this.modelJoiner = modelJoiner;
  }

  public static void main(String[] args) {
    SpringApplication.run(ParserHandler.class, args);
  }

  /**
   * See {@link less.lgeo.consumer.ParserConsumer}
   *
   * @param message
   */
  public void consume(String message) {

    Model joinedModel = modelJoiner.joinAndTransformModel(message);

    logger.info("Sending Model...");
    parserProducer.sendMessage(joinedModel);
  }

}