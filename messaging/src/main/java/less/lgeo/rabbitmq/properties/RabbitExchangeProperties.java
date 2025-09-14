package less.lgeo.rabbitmq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.exchange")
public class RabbitExchangeProperties {

    private String webToParserExchange;
    private String parserToReducerExchange;

    public String getWebToParserExchange() {
        return webToParserExchange;
    }

    public void setWebToParserExchange(String webToParserExchange) {
        this.webToParserExchange = webToParserExchange;
    }

    public String getParserToReducerExchange() {
        return parserToReducerExchange;
    }

    public void setParserToReducerExchange(String parserToReducerExchange) {
        this.parserToReducerExchange = parserToReducerExchange;
    }

}
