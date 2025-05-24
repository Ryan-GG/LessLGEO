package less.lgeo;

import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = RabbitProperties.class)
@SpringBootApplication
public class ReducerHandler implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .web(WebApplicationType.NONE)
        .sources(ReducerHandler.class)
        .build()
        .run(args);
  }


  @Override
  public void run(ApplicationArguments args) {
  }
}