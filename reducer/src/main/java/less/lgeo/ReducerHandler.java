package less.lgeo;

import less.lgeo.embedded.ModelId;
import less.lgeo.mapper.ModelMapper;
import less.lgeo.primitive.Model;
import less.lgeo.reducer.Reducer;
import less.lgeo.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReducerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

    @Autowired
    private final ModelService modelService;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final Reducer reducer;

    public ReducerHandler(
            ModelService modelService,
            ModelMapper modelMapper,
            Reducer reducer) {
        this.modelService = modelService;
        this.modelMapper = modelMapper;
        this.reducer = reducer;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReducerHandler.class);
    }


    /**
     * See {@link less.lgeo.consumer.ReducerConsumer}
     */
    public void consume(ModelId modelId) {
        modelService.getModelById(modelId)
                .map(modelMapper::toDomain)
                .ifPresentOrElse(model ->
                        {
                            Model reducedModel = reducer.reduce(model);
                        },
                        () -> logger.info("Could not find any modelId {}", modelId));
    }
}