package less.lgeo.producer;

import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private static final Logger logger = LoggerFactory.getLogger(WebServerProducer.class);
  private final RabbitTemplate rabbitTemplate;
  private final RabbitProperties rabbitProperties;

  public WebServerProducer(RabbitTemplate rabbitTemplate, RabbitProperties rabbitProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitProperties = rabbitProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitProperties.webToParserTopic(),
        rabbitProperties.webToParserRoutingKey(),
        message);
  }
}
