package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitQueueProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ParserRabbitConfig {

  @Bean
  Queue webQueueToParser(RabbitQueueProperties rabbitQueueProperties) {
    return new Queue(rabbitQueueProperties.getWebToParser(), false);
  }

  @Bean
  Queue webQueueToColorParser(RabbitQueueProperties rabbitQueueProperties) {
    return new Queue(rabbitQueueProperties.getWebToColorParser(), false);
  }

}
