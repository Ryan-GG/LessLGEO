package less.lgeo.consumer;

import less.lgeo.ColorParserHandler;
import less.lgeo.common.Color;
import less.lgeo.rabbitmq.RabbitRequestReplyProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ColorParserConsumer {

  //Needed for Spring Expression Language(SpEL) to get queue name
  @Autowired
  private final RabbitRequestReplyProperties rabbitRequestReplyProperties;

  @Autowired
  private final ColorParserHandler colorParserHandler;

  public ColorParserConsumer(RabbitRequestReplyProperties rabbitRequestReplyProperties,
      ColorParserHandler colorParserHandler) {
    this.rabbitRequestReplyProperties = rabbitRequestReplyProperties;
    this.colorParserHandler = colorParserHandler;
  }

  @RabbitListener(queues = "#{rabbitRequestReplyProperties.getQueue()}")
  public @Nullable Color handleMessage(@Payload String message) {
    return colorParserHandler.consume(message);
  }

}