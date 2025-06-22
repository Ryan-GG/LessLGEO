package less.lgeo;

import less.lgeo.rabbitmq.RabbitProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableConfigurationProperties(value = {RabbitProperties.class})
public class WebHandler {

  public static void main(String[] args) {
    SpringApplication.run(WebHandler.class, args);
  }
}
