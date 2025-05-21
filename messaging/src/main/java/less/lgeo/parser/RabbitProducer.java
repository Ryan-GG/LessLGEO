package less.lgeo.parser;

import less.lgeo.rabbitmq.RabbitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitProducer {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  public RabbitProducer(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitProperties = rabbitProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitProperties.topicName(), rabbitProperties.routingKey(),
        message);
  }
}
