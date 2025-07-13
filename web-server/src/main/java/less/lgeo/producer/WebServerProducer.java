package less.lgeo.producer;

import less.lgeo.rabbitmq.RabbitQueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitQueueProperties rabbitQueueProperties;

  public WebServerProducer(@Autowired RabbitTemplate rabbitTemplate,
      @Autowired RabbitQueueProperties rabbitQueueProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitQueueProperties = rabbitQueueProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitQueueProperties.getWebToParser(),
        message);
  }
}
