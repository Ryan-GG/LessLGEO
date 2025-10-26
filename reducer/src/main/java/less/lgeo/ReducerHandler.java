package less.lgeo;

import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Triangle;
import less.lgeo.service.ModelService;
import less.lgeo.tracer.BoundingVolumeHierarchy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ReducerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReducerHandler.class);

    @Autowired
    private final ModelService modelService;

    public ReducerHandler(ModelService modelService) {
        this.modelService = modelService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReducerHandler.class);
    }


    /**
     * See {@link less.lgeo.consumer.ReducerConsumer}
     */
    public void consume(ModelId modelId) {
        ModelEntity modelEntity = modelService.getModelById(modelId);

        Model model = modelEntity.toPojo();
        logger.info("converted: {}", model);

        List<Triangle> modelAsTriangles = model.tessellate();
        BoundingVolumeHierarchy boundingVolumeHierarchy = new BoundingVolumeHierarchy(modelAsTriangles);
        //List<Line> boundingBoxesLines = boundingVolumeHierarchy.getBoundingBoxesAsLines();
        //ModelId newBoundBoxId = modelService.insertModel(justBoundBoxes);
        //logger.info("Inserting model with bounding boxes, {}", newBoundBoxId);
    }
}