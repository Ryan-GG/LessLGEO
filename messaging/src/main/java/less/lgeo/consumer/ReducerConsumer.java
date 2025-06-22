package less.lgeo.consumer;

import less.lgeo.primitive.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ReducerConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ReducerConsumer.class);

  @RabbitListener(queues = "parser-to-reducer-queue")
  public void handleMessage(@Payload Model message) {
    logger.info("Received Model: \n {}", message.toString());
  }

}