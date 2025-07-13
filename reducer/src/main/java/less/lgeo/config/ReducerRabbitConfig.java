package less.lgeo.config;

import less.lgeo.consumer.ReducerConsumer;
import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitQueueProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReducerRabbitConfig {

  @Bean
  Queue queue(RabbitQueueProperties rabbitQueueProperties) {
    return new Queue(rabbitQueueProperties.getParserToReducer(), false);
  }


  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      RabbitQueueProperties rabbitQueueProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(rabbitQueueProperties.getParserToReducer());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ReducerConsumer reducerConsumer) {
    return new MessageListenerAdapter(reducerConsumer,
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));
  }
}
