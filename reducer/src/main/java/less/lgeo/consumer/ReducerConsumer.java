package less.lgeo.consumer;

import less.lgeo.ReducerHandler;
import less.lgeo.embedded.ModelId;
import less.lgeo.rabbitmq.properties.RabbitWorkerQueueProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "#{rabbitWorkerQueueProperties.getParserToReducer()}")
public class ReducerConsumer {

    @Autowired
    //Needed for Spring Expression Language(SpEL) to get queueName
    private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;

    @Autowired
    private final ReducerHandler reducerHandler;

    public ReducerConsumer(ReducerHandler reducerHandler,
                           RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
        this.reducerHandler = reducerHandler;
        this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
    }

    @RabbitHandler
    public void handleMessage(@Payload ModelId modelId) {
        reducerHandler.consume(modelId);
    }
}