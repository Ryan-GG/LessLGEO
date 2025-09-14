package less.lgeo.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import less.lgeo.ParserHandler;
import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.rabbitmq.properties.RabbitWebToParserRpcProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "#{rabbitWebToParserRpcProperties.getName()}")
public class ParserConsumer {

    private final ParserHandler parserHandler;

    //Needed for Spring Expression Language(SpEL) to get queueName
    private final RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties;


    public ParserConsumer(ParserHandler parserHandler, RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties) {
        this.parserHandler = parserHandler;
        this.rabbitWebToParserRpcProperties = rabbitWebToParserRpcProperties;
    }

    @RabbitHandler
    public Long handleMessage(@Payload byte[] modelJobRequest)
            throws InvalidProtocolBufferException {
        return parserHandler.consume(ModelJobRequest.parseFrom(modelJobRequest));
    }

}
