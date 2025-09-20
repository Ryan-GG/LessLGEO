package less.lgeo.producer;

import less.lgeo.rabbitmq.properties.RabbitWorkerQueueProperties;
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
        this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
    }

    public void sendMessage(Long modelId) {
        rabbitTemplate.convertAndSend(
                rabbitWorkerQueueProperties.getParserToReducer(),
                modelId);
    }
}
