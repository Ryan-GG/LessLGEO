package less.lgeo;

import java.io.File;
import less.lgeo.primitive.Model;
import less.lgeo.producer.ParserProducer;
import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = RabbitProperties.class)
public class ParserHandler implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

  private final ParserProducer parserProducer;
  private final ModelJoiner modelJoiner;

  public ParserHandler(ParserProducer parserProducer, ModelJoiner modelJoiner) {
    this.parserProducer = parserProducer;
    this.modelJoiner = modelJoiner;
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .web(WebApplicationType.NONE)
        .sources(ParserHandler.class)
        .build()
        .run(args);
  }

  @Override
  public void run(ApplicationArguments args) {

    File fileToParse = new File(args.getSourceArgs()[0]);

    Model joinedModel = modelJoiner.joinAndTransformModel(fileToParse);

    logger.info("Sending Model...");
    parserProducer.sendMessage(joinedModel);
  }
}