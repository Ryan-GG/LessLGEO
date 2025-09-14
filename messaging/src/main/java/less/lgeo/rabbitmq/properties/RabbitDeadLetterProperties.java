package less.lgeo.rabbitmq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.dead-letter")
public class RabbitDeadLetterProperties {

    private String deadLetterExchange;
    private String webToParserRoutingKey;
    private String webToParserDeadLetterQueue;
    private String parserToReducerDeadLetterQueue;
    private String parserToReducerRoutingKey;


    public String getParserToReducerDeadLetterQueue() {
        return parserToReducerDeadLetterQueue;
    }

    public void setParserToReducerDeadLetterQueue(String parserToReducerDeadLetterQueue) {
        this.parserToReducerDeadLetterQueue = parserToReducerDeadLetterQueue;
    }

    public String getWebToParserDeadLetterQueue() {
        return webToParserDeadLetterQueue;
    }

    public void setWebToParserDeadLetterQueue(String webToParserDeadLetterQueue) {
        this.webToParserDeadLetterQueue = webToParserDeadLetterQueue;
    }

    public String getDeadLetterExchange() {
        return deadLetterExchange;
    }

    public void setDeadLetterExchange(String deadLetterExchange) {
        this.deadLetterExchange = deadLetterExchange;
    }

    public String getParserToReducerRoutingKey() {
        return parserToReducerRoutingKey;
    }

    public void setParserToReducerRoutingKey(String parserToReducerRoutingKey) {
        this.parserToReducerRoutingKey = parserToReducerRoutingKey;
    }

    public String getWebToParserRoutingKey() {
        return webToParserRoutingKey;
    }

    public void setWebToParserRoutingKey(String webToParserRoutingKey) {
        this.webToParserRoutingKey = webToParserRoutingKey;
    }
}
