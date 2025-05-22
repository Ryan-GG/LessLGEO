package less.lgeo.rabbitmq;

import less.lgeo.primitive.Model;
import less.lgeo.reducer.ReducerConsumer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


  @Bean
  Queue queue(RabbitProperties rabbitProperties) {
    return new Queue(rabbitProperties.queueName(), false);
  }

  @Bean
  TopicExchange exchange(RabbitProperties rabbitProperties) {
    return new TopicExchange(rabbitProperties.topicName());
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange, RabbitProperties rabbitProperties) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(rabbitProperties.routingKey());
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      RabbitProperties rabbitProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(rabbitProperties.queueName());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ReducerConsumer reducerConsumer) {
    return new MessageListenerAdapter(reducerConsumer,
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));
  }
}
