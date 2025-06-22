package less.lgeo.consumer;

import less.lgeo.ParserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ParserConsumer {

  private static final Logger logger = LoggerFactory.getLogger(ParserConsumer.class);
  private final ParserHandler parserHandler;

  public ParserConsumer(ParserHandler parserHandler) {
    this.parserHandler = parserHandler;
  }

  @RabbitListener(queues = "web-to-parser-queue")
  public void handleMessage(@Payload String message) {
    parserHandler.consume(message);
  }

}
