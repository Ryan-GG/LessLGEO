package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitRequestReplyProperties;
import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableRabbit
@Configuration
class ParserRabbitConfig {

  @Profile("lDrawParser")
  private static class LDrawParserConfig {

    @Bean
    Queue webQueueToParser(RabbitWorkerQueueProperties rabbitWorkerQueueProperties) {
      return new Queue(rabbitWorkerQueueProperties.getWebToParser(), false);
    }
  }

  @Profile("colorParser")
  private static class ColorParserConfig {

    @Bean
    Queue webQueueToColorParser(RabbitRequestReplyProperties rabbitRequestReplyProperties) {
      return new Queue(rabbitRequestReplyProperties.getQueue(), false);
    }

    @Bean
    DirectExchange webExchangeToColorParser(
        RabbitRequestReplyProperties rabbitRequestReplyProperties) {
      return new DirectExchange(rabbitRequestReplyProperties.getExchange());
    }

    @Bean
    Binding webBindingToColorParser(RabbitRequestReplyProperties rabbitRequestReplyProperties,
        DirectExchange exchange, Queue queue) {
      return BindingBuilder.bind(queue)
          .to(exchange)
          .with(rabbitRequestReplyProperties.getRoutingKey());
    }
  }

}
