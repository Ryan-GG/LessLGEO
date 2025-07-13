package less.lgeo.config;

import less.lgeo.consumer.ReducerConsumer;
import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.ParserToReducerProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReducerRabbitConfig {

  @Bean
  Queue queue(ParserToReducerProperties parserToReducerProperties) {
    return new Queue(parserToReducerProperties.getQueue(), false);
  }

  @Bean
  DirectExchange exchange(ParserToReducerProperties parserToReducerProperties) {
    return new DirectExchange(parserToReducerProperties.getExchange());
  }

  @Bean
  Binding binding(Queue queue, DirectExchange exchange,
      ParserToReducerProperties parserToReducerProperties) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(parserToReducerProperties.getRoutingKey());
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      ParserToReducerProperties parserToReducerProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(parserToReducerProperties.getQueue());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ReducerConsumer reducerConsumer) {
    return new MessageListenerAdapter(reducerConsumer,
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));
  }
}
