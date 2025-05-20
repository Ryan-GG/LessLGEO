package less.lgeo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

@ConfigurationProperties(prefix = "rabbit")
public record RabbitProperties(
    @NonNull String topicName,
    @NonNull String queueName,
    @NonNull String routingKey) {

}
