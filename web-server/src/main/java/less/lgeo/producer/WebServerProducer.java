package less.lgeo.producer;

import less.lgeo.rabbitmq.WebToParserProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private final RabbitTemplate rabbitTemplate;
  private final WebToParserProperties webToParserProperties;

  public WebServerProducer(@Autowired RabbitTemplate rabbitTemplate,
      @Autowired WebToParserProperties webToParserProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.webToParserProperties = webToParserProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(webToParserProperties.getExchange(),
        webToParserProperties.getRoutingKey(),
        message);
  }
}
