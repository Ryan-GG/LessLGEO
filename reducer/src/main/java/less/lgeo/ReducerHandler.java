package less.lgeo;

import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.mapper.ModelMapper;
import less.lgeo.primitive.Model;
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

    public ReducerHandler(ModelService modelService, ModelMapper modelMapper) {
        this.modelService = modelService;
        this.modelMapper = modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReducerHandler.class);
    }


    /**
     * See {@link less.lgeo.consumer.ReducerConsumer}
     */
    public void consume(ModelId modelId) {
        ModelEntity modelEntity = modelService.getModelById(modelId).orElseThrow();

        Model model = modelMapper.toDomain(modelEntity);
        logger.info("converted: {}", model);
    }
}