package less.lgeo;

import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {RabbitProperties.class})
public class ReducerHandler {

  private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

  public static void main(String[] args) {
    SpringApplication.run(ReducerHandler.class);
  }


  public void consume(Model message) {
    logger.info("Consuming: {}", message);
  }
}