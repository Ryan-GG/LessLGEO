package less.lgeo.rabbitmq.config;

import less.lgeo.rabbitmq.properties.RabbitDeadLetterProperties;
import less.lgeo.rabbitmq.properties.RabbitExchangeProperties;
import less.lgeo.rabbitmq.properties.RabbitWebToParserRpcProperties;
import less.lgeo.rabbitmq.properties.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
class RabbitConfig {

    @Bean
    Queue webToParserQueue(
            RabbitWebToParserRpcProperties rabbitWebToParserRpcProperties,
            RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return QueueBuilder.durable(rabbitWebToParserRpcProperties.getName())
                .deadLetterExchange(rabbitDeadLetterProperties.getDeadLetterExchange())
                .deadLetterRoutingKey(rabbitDeadLetterProperties.getWebToParserRoutingKey())
                .build();
    }

    @Bean
    public DirectExchange webToParserExchange(RabbitExchangeProperties rabbitExchangeProperties) {
        return new DirectExchange(rabbitExchangeProperties.getWebToParserExchange());
    }

    @Bean
    public DirectExchange deadLetterExchange(RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return new DirectExchange(rabbitDeadLetterProperties.getDeadLetterExchange());
    }

    @Bean
    public Binding webToParserBinding(
            Queue webToParserQueue,
            DirectExchange webToParserExchange) {
        return BindingBuilder.bind(webToParserQueue).to(webToParserExchange).withQueueName();
    }

    @Bean
    Queue parserToReducerQueue(
            RabbitWorkerQueueProperties rabbitWorkerQueueProperties,
            RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return QueueBuilder.durable(rabbitWorkerQueueProperties.getParserToReducer())
                .deadLetterExchange(rabbitDeadLetterProperties.getDeadLetterExchange())
                .deadLetterRoutingKey(rabbitDeadLetterProperties.getParserToReducerRoutingKey())
                .build();
    }

    @Bean
    Queue webToParserDeadLetterQueue(RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return new Queue(rabbitDeadLetterProperties.getWebToParserDeadLetterQueue(), true);
    }

    @Bean
    Queue parserToReducerDeadLetterQueue(RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return new Queue(rabbitDeadLetterProperties.getParserToReducerDeadLetterQueue(), true);
    }

    @Bean
    public DirectExchange parserToReducerExchange(RabbitExchangeProperties rabbitExchangeProperties) {
        return new DirectExchange(rabbitExchangeProperties.getParserToReducerExchange());
    }

    @Bean
    public Binding parserToReducerBinding(
            Queue parserToReducerQueue,
            DirectExchange parserToReducerExchange) {
        return BindingBuilder.bind(parserToReducerQueue).to(parserToReducerExchange).withQueueName();
    }

    @Bean
    public Binding parserToReduceDeadLetterBinding(
            Queue parserToReducerDeadLetterQueue,
            DirectExchange deadLetterExchange,
            RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return BindingBuilder.bind(parserToReducerDeadLetterQueue).to(deadLetterExchange)
                .with(rabbitDeadLetterProperties.getParserToReducerRoutingKey());
    }

    @Bean
    public Binding webToParserDeadLetterBinding(
            Queue webToParserDeadLetterQueue,
            DirectExchange deadLetterExchange,
            RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return BindingBuilder.bind(webToParserDeadLetterQueue).to(deadLetterExchange)
                .with(rabbitDeadLetterProperties.getWebToParserRoutingKey());
    }

}
