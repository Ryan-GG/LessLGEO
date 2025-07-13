package less.lgeo.producer;

import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.ParserToReducerProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ParserProducer {

  private final RabbitTemplate rabbitTemplate;
  private final ParserToReducerProperties parserToReducerProperties;

  public ParserProducer(RabbitTemplate rabbitTemplate,
      ParserToReducerProperties parserToReducerProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitTemplate.setMessageConverter(
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));

    this.parserToReducerProperties = parserToReducerProperties;
  }

  public void sendMessage(Model message) {
    rabbitTemplate.convertAndSend(parserToReducerProperties.getExchange(),
        parserToReducerProperties.getRoutingKey(),
        message);
  }
}
