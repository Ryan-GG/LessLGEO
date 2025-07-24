package less.lgeo.producer;


import java.util.UUID;
import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebServerProducer {

  private final Logger logger = LoggerFactory.getLogger( WebServerProducer.class );

  @Autowired
  private final RabbitTemplate rabbitTemplate;
  @Autowired
  private final RabbitWorkerQueueProperties rabbitWorkerQueueProperties;

  public WebServerProducer( RabbitTemplate rabbitTemplate,
      RabbitWorkerQueueProperties rabbitWorkerQueueProperties
  ) {
    this.rabbitTemplate = rabbitTemplate;
    this.rabbitWorkerQueueProperties = rabbitWorkerQueueProperties;
  }

  /**
   * See at `less.lgeo.consumer.ParserConsumer`
   */
  public void sendMessage( UUID uuid, String message ) {
    ModelJobRequest modelJobRequest = ModelJobRequest.newBuilder()
        .setUUID( uuid.toString() )
        .setModelString( message )
        .build();

    rabbitTemplate.convertAndSend( rabbitWorkerQueueProperties.getWebToParser(),
        modelJobRequest.toByteArray() );
  }

}
