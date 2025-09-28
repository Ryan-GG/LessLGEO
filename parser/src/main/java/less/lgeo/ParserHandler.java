package less.lgeo;

import less.lgeo.embedded.ModelId;
import less.lgeo.messaging.ModelJobRequest;
import less.lgeo.primitive.Model;
import less.lgeo.producer.ParserProducer;
import less.lgeo.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserHandler {

    private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

    @Autowired
    private final ParserProducer parserProducer;

    @Autowired
    private final ModelService modelService;

    @Autowired
    private final ModelJoiner modelJoiner;

    public ParserHandler(
            ParserProducer parserProducer,
            ModelService modelService,
            ModelJoiner modelJoiner) {
        this.parserProducer = parserProducer;
        this.modelService = modelService;
        this.modelJoiner = modelJoiner;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParserHandler.class, args);
    }

    /**
     * See {@link less.lgeo.consumer.ParserConsumer}
     *
     * @param modelJobRequest Model LDraw String
     */
    public Long consume(ModelJobRequest modelJobRequest) {

        Model joinedModel = modelJoiner.joinAndTransformModel(modelJobRequest);

        ModelId modelId = modelService.insertModel(joinedModel);
        logger.info("Inserted Model: {}", modelId);

        //FIXME, Switch from lonog to ModelId
        logger.info("Sending Model {}", modelId);
        parserProducer.sendMessage(modelId.getValue());

        return modelId.getValue();
    }

}