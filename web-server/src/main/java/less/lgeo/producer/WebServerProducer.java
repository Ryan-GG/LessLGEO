package less.lgeo.producer;


import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.rabbitmq.RabbitWebToParserRpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

    private final Logger logger = LoggerFactory.getLogger(WebServerProducer.class);

    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties;

    public WebServerProducer(RabbitTemplate rabbitTemplate,
                             RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitWebToParserRpcProperties = rabbitWebToParserRpcProperties;

        long replyTimeout_MILLIS = this.rabbitWebToParserRpcProperties.getReplyTimeout().toMillis();
        this.rabbitTemplate.setReplyTimeout(replyTimeout_MILLIS);
    }

    /**
     * See at `less.lgeo.consumer.ParserConsumer`
     */
    public @Nullable Long sendMessage(String message) {
        ModelJobRequest modelJobRequest = ModelJobRequest.newBuilder()
                .setModelString(message)
                .build();
        
        return rabbitTemplate.convertSendAndReceiveAsType(
                rabbitWebToParserRpcProperties.getName(),
                modelJobRequest.toByteArray(),
                ParameterizedTypeReference.forType(Long.TYPE));
    }
}
