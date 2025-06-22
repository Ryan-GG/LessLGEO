package less.lgeo.consumer;

import less.lgeo.ReducerHandler;
import less.lgeo.primitive.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ReducerConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ReducerConsumer.class);
  private final ReducerHandler reducerHandler;

  public ReducerConsumer(ReducerHandler reducerHandler) {
    this.reducerHandler = reducerHandler;
  }

  @RabbitListener(queues = "parser-to-reducer-queue")
  public void handleMessage(@Payload Model message) {
    reducerHandler.consume(message);
  }

}