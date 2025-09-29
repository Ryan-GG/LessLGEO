package less.lgeo;

import less.lgeo.embedded.ModelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReducerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

    public static void main(String[] args) {
        SpringApplication.run(ReducerHandler.class);
    }


    /**
     * See {@link less.lgeo.consumer.ReducerConsumer}
     */
    public void consume(ModelId modelId) {
        logger.info("Consuming: {}", modelId);
    }
}