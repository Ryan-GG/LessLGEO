package less.lgeo.config;

import less.lgeo.consumer.ParserConsumer;
import less.lgeo.rabbitmq.WebToParserProperties;
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
class ParserRabbitConfig {

  @Bean
  DirectExchange webExchangeToParser(WebToParserProperties webToParserProperties) {
    return new DirectExchange(webToParserProperties.getExchange());
  }

  @Bean
  Queue webQueueToParser(WebToParserProperties webToParserProperties) {
    return new Queue(webToParserProperties.getQueue(), false);
  }

  @Bean
  Binding binding(Queue queue, DirectExchange exchange,
      WebToParserProperties webToParserProperties) {
    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(webToParserProperties.getRoutingKey());
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter,
      WebToParserProperties webToParserProperties) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(webToParserProperties.getQueue());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(ParserConsumer parserConsumer) {
    return new MessageListenerAdapter(parserConsumer);
  }
}
