package less.lgeo.producer;

import com.google.protobuf.InvalidProtocolBufferException;
import less.lgeo.common.Color;
import less.lgeo.rabbitmq.RabbitRequestReplyProperties;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private final Logger logger = LoggerFactory.getLogger(WebServerProducer.class);

  @Autowired
  private final RabbitTemplate rabbitTemplate;
  @Autowired
  private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;
  @Autowired
  private final RabbitRequestReplyProperties rabbitRequestReplyProperties;

  public WebServerProducer(RabbitTemplate rabbitTemplate,
      RabbitWorkerQueueProperties rabbitWorkerQueueProperties,
      RabbitRequestReplyProperties rabbitRequestReplyProperties
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
    this.rabbitRequestReplyProperties = rabbitRequestReplyProperties;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(rabbitWorkerQueueProperties.getWebToParser(),
        message);
  }

  public @Nullable Color sendAndReceiveColor(String message) {
    Message receivedMessage = rabbitTemplate.sendAndReceive(
        rabbitRequestReplyProperties.getExchange(),
        rabbitRequestReplyProperties.getRoutingKey(),
        new Message(message.getBytes()));

    if (receivedMessage == null) {
      return null;
    }
    try {
      return Color.parseFrom(receivedMessage.getBody());
    } catch (InvalidProtocolBufferException e) {
      logger.error("Received Message: {}", receivedMessage);
      logger.error("Failed to parse Color", e);
      return null;
    }
  }
}
