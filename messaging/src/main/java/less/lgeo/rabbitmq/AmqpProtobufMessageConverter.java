package less.lgeo.rabbitmq;

import com.google.protobuf.MessageLite;
import java.util.Collections;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.ProtobufMessageConverter;
import org.springframework.messaging.support.GenericMessage;

public class AmqpProtobufMessageConverter implements MessageConverter {

  private final ProtobufMessageConverter delegate;
  private final MessageLite prototype;

  public AmqpProtobufMessageConverter(MessageLite prototype) {
    this.delegate = new ProtobufMessageConverter();
    this.prototype = prototype;
  }

  @Override
  public Message toMessage(@NonNull Object object, @NonNull MessageProperties messageProperties) {
    org.springframework.messaging.Message<?> delegateToSpringMessage =
        delegate.toMessage(object, new MessageHeaders(messageProperties.getHeaders()));

    byte[] payload = (byte[]) delegateToSpringMessage.getPayload();

    messageProperties.setContentType(ProtobufMessageConverter.PROTOBUF.toString());
    return new Message(payload, messageProperties);
  }

  @Override
  public Object fromMessage(Message message) {
    byte[] body = message.getBody();
    org.springframework.messaging.Message<byte[]> messagingMessage =
        new GenericMessage<>(body, new MessageHeaders(Collections.emptyMap()));

    return delegate.fromMessage(messagingMessage, prototype.getClass());
  }
}
