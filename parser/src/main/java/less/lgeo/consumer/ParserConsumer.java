package less.lgeo.consumer;

import less.lgeo.ParserHandler;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "web-to-parser-queue")
public class ParserConsumer {

  private final ParserHandler parserHandler;

  public ParserConsumer(ParserHandler parserHandler) {
    this.parserHandler = parserHandler;
  }

  @RabbitHandler
  public void handleMessage(@Payload String message) {
    parserHandler.consume(message);
  }

}
