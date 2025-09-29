package less.lgeo.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import less.lgeo.ParserHandler;
import less.lgeo.embedded.ModelId;
import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.rabbitmq.properties.RabbitWebToParserRpcProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "#{rabbitWebToParserRpcProperties.getName()}")
public class ParserConsumer {

    @Autowired
    private final ParserHandler parserHandler;

    //Needed for Spring Expression Language(SpEL) to get queueName
    @Autowired
    private final RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties;


    public ParserConsumer(ParserHandler parserHandler,
                          RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties) {
        this.parserHandler = parserHandler;
        this.rabbitWebToParserRpcProperties = rabbitWebToParserRpcProperties;
    }

    @RabbitHandler
    public ModelId handleMessage(@Payload byte[] modelJobRequest) throws InvalidProtocolBufferException {
        return parserHandler.consume(ModelJobRequest.parseFrom(modelJobRequest));
    }

}
