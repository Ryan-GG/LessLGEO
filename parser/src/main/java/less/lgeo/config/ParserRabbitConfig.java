package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
class ParserRabbitConfig {

  @Bean
  Queue webQueueToParser( RabbitWorkerQueueProperties rabbitWorkerQueueProperties ) {
    return new Queue( rabbitWorkerQueueProperties.getWebToParser(), false );
  }

}
