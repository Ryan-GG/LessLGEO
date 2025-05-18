package less.lgeo;

import java.io.File;
import java.util.concurrent.TimeUnit;
import less.lgeo.parse.Parser;
import less.lgeo.rabbitmq.RabbitBroker;
import less.lgeo.rabbitmq.RabbitReceiver;
import less.lgeo.set.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserHandler implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);
  private final RabbitTemplate rabbitTemplate;
  private final RabbitReceiver receiver;

  public ParserHandler(RabbitReceiver receiver, RabbitTemplate rabbitTemplate) {
    this.receiver = receiver;
    this.rabbitTemplate = rabbitTemplate;
  }

  public static void main(String[] args) {
    SpringApplication bootApplication = new SpringApplication(ParserHandler.class);
    bootApplication.setWebApplicationType(WebApplicationType.NONE);
    bootApplication.run(args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {

    Parser parser = new Parser();
    File fileToParse = new File(args.getSourceArgs()[0]);

    Model model = parser.parse(fileToParse);

    logger.info("Model result: {}", model.toString());

    System.out.println("Sending message...");
    rabbitTemplate.convertAndSend(RabbitBroker.topicExchangeName, "foo.bar.baz",
        "Hello from RabbitMQ!");
    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
  }
}