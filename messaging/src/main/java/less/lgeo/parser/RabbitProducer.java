package less.lgeo.parser;

import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  public RabbitProducer(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitTemplate.setMessageConverter(
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));

    this.rabbitProperties = rabbitProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitProperties.topicName(), rabbitProperties.routingKey(),
        message);
  }

  public void sendMessage(Model message) {
    rabbitTemplate.convertAndSend(rabbitProperties.topicName(), rabbitProperties.routingKey(),
        message);
  }
}
