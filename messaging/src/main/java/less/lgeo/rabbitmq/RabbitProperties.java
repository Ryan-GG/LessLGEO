package less.lgeo.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

@ConfigurationProperties(prefix = "rabbit")
public record RabbitProperties(
    @NonNull String parserToReducerTopic,
    @NonNull String parserToReducerQueue,
    @NonNull String parserToReducerRoutingKey,
    @NonNull String webToParserTopic,
    @NonNull String webToParserQueue,
    @NonNull String webToParserRoutingKey) {

}
