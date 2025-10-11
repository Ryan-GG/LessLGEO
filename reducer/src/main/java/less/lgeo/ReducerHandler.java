package less.lgeo;

import static less.lgeo.entity.ModelEntity.toGpb;
import static less.lgeo.primitive.ModelUtils.splitIntoTriangles;

import java.util.List;
import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.Triangle;
import less.lgeo.service.ModelService;
import less.lgeo.tracer.BoundingVolumeHierarchy;
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
    ModelEntity model = modelService.getModelById(modelId);

    Model converted = toGpb(model);
    logger.info("converted: {}", converted);

    List<Triangle> triangles = splitIntoTriangles(converted);
    BoundingVolumeHierarchy bvh = new BoundingVolumeHierarchy(triangles);
    List<Line> boundingBoxesLines = bvh.getBoundingBoxes();
    Model justBoundBoxes = converted.toBuilder()
        .clearTriangle()
        .clearQuadrilateral()
        .clearLine()
        .clearOptionalLine()
        .clearPiece()
        .addAllLine(boundingBoxesLines)
        .build();

    ModelId newBoundBoxId = modelService.insertModel(justBoundBoxes);
    logger.info("Inserting model with bounding boxes, {}", newBoundBoxId);
    logger.info("BVH: {}", bvh);
  }
}