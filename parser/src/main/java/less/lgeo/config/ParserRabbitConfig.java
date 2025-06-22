package less.lgeo.config;

import less.lgeo.consumer.ParserConsumer;
import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.AmqpProtobufMessageConverter;
import less.lgeo.rabbitmq.RabbitProperties;
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
public class ParserRabbitConfig {

  @Bean
  Queue queue(RabbitProperties rabbitProperties) {
    return new Queue(rabbitProperties.webToParserQueue(), false);
  }

  @Bean
  TopicExchange exchange(RabbitProperties rabbitProperties) {
    return new TopicExchange(rabbitProperties.webToParserTopic());
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange, RabbitProperties rabbitProperties) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(rabbitProperties.webToParserRoutingKey());
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      RabbitProperties rabbitProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(rabbitProperties.webToParserQueue());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ParserConsumer parserConsumer) {
    return new MessageListenerAdapter(parserConsumer,
        new AmqpProtobufMessageConverter(Model.getDefaultInstance()));
  }
}
