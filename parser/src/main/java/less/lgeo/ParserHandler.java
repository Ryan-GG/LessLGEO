package less.lgeo;

import java.io.File;
import less.lgeo.parser.Parser;
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

@EnableConfigurationProperties(value = RabbitProperties.class)
@SpringBootApplication
public class ParserHandler implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

  private final ParserProducer parserProducer;

  public ParserHandler(ParserProducer parserProducer) {
    this.parserProducer = parserProducer;
  }

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .web(WebApplicationType.NONE)
        .sources(ParserHandler.class)
        .build()
        .run(args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    Parser parser = new Parser();
    File fileToParse = new File(args.getSourceArgs()[0]);

    Model model = parser.parse(fileToParse);

    logger.info("Model result: {}", model);

    logger.info("Sending Model...");
    parserProducer.sendMessage(model);
  }
}