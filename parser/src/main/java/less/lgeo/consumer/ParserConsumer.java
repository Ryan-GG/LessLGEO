package less.lgeo.consumer;

import com.google.protobuf.InvalidProtocolBufferException;
import less.lgeo.ParserHandler;
import less.lgeo.messaging.ModelJobRequest;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RabbitListener( queues = "web-to-parser-queue" )
public class ParserConsumer {

  private final ParserHandler parserHandler;

  public ParserConsumer( ParserHandler parserHandler ) {
    this.parserHandler = parserHandler;
  }

  @RabbitHandler
  public void handleMessage( @Payload byte[] modelJobRequest )
      throws InvalidProtocolBufferException {
    parserHandler.consume( ModelJobRequest.parseFrom( modelJobRequest ) );
  }

}
