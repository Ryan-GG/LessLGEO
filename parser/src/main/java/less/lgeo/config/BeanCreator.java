package less.lgeo.config;

import less.lgeo.parse.Parser;
import org.springframework.context.annotation.Bean;


public class BeanCreator {

  @Bean
  public Parser createParser() {
    return new Parser();
  }
}
