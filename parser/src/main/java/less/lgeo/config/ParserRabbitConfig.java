package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitExchangeProperties;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
class ParserRabbitConfig {

  @Bean
  Queue webToParserQueue(RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
    return new Queue(rabbitWorkerQueueProperties.getWebToParser(), true);
  }

  @Bean
  public DirectExchange webToParserExchange(RabbitExchangeProperties rabbitExchangeProperties) {
    return new DirectExchange(rabbitExchangeProperties.getWebToParserExchange());
  }

  @Bean
  public Binding webToParserBinding(Queue webToParserQueue, DirectExchange webToParserExchange) {
    return BindingBuilder.bind(webToParserQueue).to(webToParserExchange).withQueueName();
  }

  @Bean
  Queue parserToReducerQueue(RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
    return new Queue(rabbitWorkerQueueProperties.getParserToReducer(), true);
  }

  @Bean
  public DirectExchange parserToReducerExchange(RabbitExchangeProperties rabbitExchangeProperties) {
    return new DirectExchange(rabbitExchangeProperties.getParserToReducerExchange());
  }

  @Bean
  public Binding parserToReducerBinding(Queue parserToReducerQueue,
      DirectExchange parserToReducerExchange) {
    return BindingBuilder.bind(parserToReducerQueue).to(parserToReducerExchange).withQueueName();
  }

}
