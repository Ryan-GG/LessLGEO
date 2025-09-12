package less.lgeo.producer;


import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.rabbitmq.RabbitRpcProperties;
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
    private final RabbitRpcProperties rabbitRpcProperties;

    public WebServerProducer(RabbitTemplate rabbitTemplate,
                             RabbitRpcProperties rabbitRpcProperties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitRpcProperties = rabbitRpcProperties;
    }

    /**
     * See at `less.lgeo.consumer.ParserConsumer`
     */
    public Long sendMessage(String message) {
        ModelJobRequest modelJobRequest = ModelJobRequest.newBuilder()
                .setModelString(message)
                .build();

        return (Long) rabbitTemplate.convertSendAndReceive(rabbitRpcProperties.getWebToParser(),
                modelJobRequest.toByteArray());
    }

}
