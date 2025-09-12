package less.lgeo.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.worker-queue")
public class RabbitWorkerQueueProperties {

    private String parserToReducer;

    public String getParserToReducer() {
        return parserToReducer;
    }

    public void setParserToReducer(String parserToReducer) {
        this.parserToReducer = parserToReducer;
    }

}
