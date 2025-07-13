package less.lgeo.producer;

import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitQueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserProducer {

  @Autowired
  private final RabbitTemplate rabbitTemplate;

  @Autowired
  private final RabbitQueueProperties rabbitQueueProperties;

  public ParserProducer(RabbitTemplate rabbitTemplate,
      RabbitQueueProperties rabbitQueueProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitTemplate.setMessageConverter(
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));

    this.rabbitQueueProperties = rabbitQueueProperties;
  }

  public void sendMessage(Model message) {
    rabbitTemplate.convertAndSend(
        rabbitQueueProperties.getParserToReducer(),
        message);
  }
}
