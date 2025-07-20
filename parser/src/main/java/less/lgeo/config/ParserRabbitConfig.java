package less.lgeo.config;

import less.lgeo.rabbitmq.RabbitWorkerQueueProperties;
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

}
