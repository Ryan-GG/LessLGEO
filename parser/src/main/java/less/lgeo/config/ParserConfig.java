package less.lgeo.config;

import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import org.springframework.context.annotation.Bean;

public class ParserConfig {

    @Bean
    public LDrawParser createLDrawParser() {
        return new LDrawParser();
    }

    @Bean
    public ConnectivityParser createConnectivityParser() {
        return new ConnectivityParser();
    }

}
