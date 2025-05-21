package less.lgeo.rabbitmq;

import less.lgeo.primitive.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitReceiver {

  private static final Logger logger = LoggerFactory.getLogger(RabbitReceiver.class);

  @RabbitListener(queues = "parser-to-reducer-queue")
  public void handleMessage(Model message) {
    logger.info("Received Model GPB \n {}", message.toString());
  }

}