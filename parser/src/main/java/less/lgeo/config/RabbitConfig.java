package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitDeadLetterProperties;
import less.lgeo.rabbitmq.RabbitExchangeProperties;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
class RabbitConfig {

    @Bean
    Queue webToParserQueue(
            RabbitWorkerQueueProperties rabbitWorkerQueueProperties,
            RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return QueueBuilder.durable(rabbitWorkerQueueProperties.getWebToParser())
                .deadLetterExchange(rabbitDeadLetterProperties.getDeadLetterExchange())
                .deadLetterRoutingKey(rabbitDeadLetterProperties.getWebToParserRoutingKey())
                .build();
    }

    @Bean
    public DirectExchange webToParserExchange(RabbitExchangeProperties rabbitExchangeProperties) {
        return new DirectExchange(rabbitExchangeProperties.getWebToParserExchange());
    }

    @Bean
    public FanoutExchange deadLetterExchange(RabbitDeadLetterProperties rabbitDeadLetterProperties) {
        return new FanoutExchange(rabbitDeadLetterProperties.getDeadLetterExchange());
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
            FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(parserToReducerDeadLetterQueue).to(deadLetterExchange);
    }

    @Bean
    public Binding webToParserDeadLetterBinding(
            Queue webToParserDeadLetterQueue,
            FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(webToParserDeadLetterQueue).to(deadLetterExchange);
    }

}
