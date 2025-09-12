package less.lgeo.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.rpc")
public class RabbitRpcProperties {

    private String webToParser;

    public String getWebToParser() {
        return webToParser;
    }

    public void setWebToParser(String webToParser) {
        this.webToParser = webToParser;
    }
    
}

