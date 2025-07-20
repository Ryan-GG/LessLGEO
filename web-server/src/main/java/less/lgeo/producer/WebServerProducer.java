package less.lgeo.producer;


import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private final Logger logger = LoggerFactory.getLogger(WebServerProducer.class);

  @Autowired
  private final RabbitTemplate rabbitTemplate;
  @Autowired
  private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;

  public WebServerProducer(RabbitTemplate rabbitTemplate,
      RabbitWorkerQueueProperties rabbitWorkerQueueProperties
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitWorkerQueueProperties.getWebToParser(),
        message);
  }

}
