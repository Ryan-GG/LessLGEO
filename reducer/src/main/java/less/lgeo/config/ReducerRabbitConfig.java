package less.lgeo.config;

import less.lgeo.consumer.ReducerConsumer;
import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class ReducerRabbitConfig {

  @Bean
  Queue queue(RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
    return new Queue(rabbitWorkerQueueProperties.getParserToReducer(), false);
  }


  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(rabbitWorkerQueueProperties.getParserToReducer());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ReducerConsumer reducerConsumer) {
    return new MessageListenerAdapter(reducerConsumer,
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));
  }
}
