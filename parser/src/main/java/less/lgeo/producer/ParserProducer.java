package less.lgeo.producer;

import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserProducer {

  @Autowired
  private final RabbitTemplate rabbitTemplate;

  @Autowired
  private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;

  public ParserProducer(RabbitTemplate rabbitTemplate,
      RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitTemplate.setMessageConverter(
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));

    this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
  }

  public void sendMessage(Model message) {
    rabbitTemplate.convertAndSend(
        rabbitWorkerQueueProperties.getParserToReducer(),
        message);
  }
}
