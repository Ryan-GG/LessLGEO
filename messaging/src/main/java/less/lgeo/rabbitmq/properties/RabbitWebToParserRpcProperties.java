package less.lgeo.rabbitmq.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.rpc.web-to-parser")
public class RabbitWebToParserRpcProperties {

    private String name;
    private Duration replyTimeout;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getReplyTimeout() {
        return this.replyTimeout;
    }

    public void setReplyTimeout(Duration replyTimeout) {
        this.replyTimeout = replyTimeout;
    }

}

