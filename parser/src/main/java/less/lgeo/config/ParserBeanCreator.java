package less.lgeo.config;

import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.service.ColorService;
import org.springframework.context.annotation.Bean;

public class ParserBeanCreator {


  @Bean
  public LDrawParser createLDrawParser(ColorService colorService) {
    return new LDrawParser(colorService);
  }

  @Bean
  public ConnectivityParser createConnectivityParser() {
    return new ConnectivityParser();
  }

}
