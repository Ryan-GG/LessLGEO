package less.lgeo.producer;

import less.lgeo.embedded.ModelId;
import less.lgeo.rabbitmq.properties.RabbitWorkerQueueProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParserProducer {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;

    public ParserProducer(RabbitTemplate rabbitTemplate,
                          MessageConverter messageConverter,
                          RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
        this.rabbitTemplate.setMessageConverter(messageConverter);
    }

    public void sendMessage(ModelId modelId) {
        rabbitTemplate.convertAndSend(
                rabbitWorkerQueueProperties.getParserToReducer(),
                modelId);
    }
}
