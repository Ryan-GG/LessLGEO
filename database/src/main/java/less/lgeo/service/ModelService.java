package less.lgeo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.entity.LineEntity;
import less.lgeo.entity.ModelEntity;
import less.lgeo.entity.OptionalLineEntity;
import less.lgeo.entity.QuadrilateralEntity;
import less.lgeo.entity.TriangleEntity;
import less.lgeo.primitive.Line;
import less.lgeo.primitive.Model;
import less.lgeo.primitive.OptionalLine;
import less.lgeo.primitive.Quadrilateral;
import less.lgeo.primitive.Triangle;
import less.lgeo.repository.ColorRepository;
import less.lgeo.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Fetches {@link less.lgeo.entity.ModelEntity} from the database
 */
@Service
public class ModelService {

  private static final Logger logger = LoggerFactory.getLogger(ModelService.class);

  private final ModelRepository modelRepository;
  private final ColorRepository colorRepository;

  public ModelService(
      ModelRepository modelRepository,
      ColorRepository colorRepository) {
    this.modelRepository = modelRepository;
    this.colorRepository = colorRepository;
  }

  /**
   * Convert a GPB Model to a ModelEntity recursively.
   */
  private ModelEntity createModelEntity(Model gpb, ModelEntity parent) {
    UUID modelUUID = UUID.fromString(gpb.getUUID());

    ModelEntity entity = new ModelEntity();
    entity.setId(modelUUID);

    entity.setLines(
        gpb.getLineList().stream().map(line -> createLineEntity(modelUUID, line)).toList());
    entity.setTriangles(
        gpb.getTriangleList().stream().map(triangle -> createTriangleEntity(modelUUID, triangle))
            .toList());
    entity.setQuadrilaterals(
        gpb.getQuadrilateralList().stream()
            .map(quadrilateral -> createQuadrilateralEntity(modelUUID, quadrilateral)).toList());
    entity.setOptionalLines(
        gpb.getOptionalLineList().stream()
            .map(optionalLine -> createOptionalLineEntity(modelUUID, optionalLine)).toList());
    entity.setParent(parent);

    // Recursively convert children
    List<ModelEntity> childEntities = gpb.getPieceList().stream()
        .map(subModelRef -> createModelEntity(subModelRef.getSubModel(), entity))
        .toList();
    entity.setChildren(childEntities);

    return entity;
  }


  private LineEntity createLineEntity(UUID parentModelId, Line gpb) {
    return new LineEntity(
        parentModelId,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()));
  }

  /**
   * Creates a Triangle Entity to be stored into the model_triangle table
   */
  private OptionalLineEntity createOptionalLineEntity(UUID parentModelId, OptionalLine gpb) {
    return new OptionalLineEntity(
        parentModelId,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()),
        new VertexEmbeddable(gpb.getP4()));
  }

  private QuadrilateralEntity createQuadrilateralEntity(UUID parentModelId, Quadrilateral gpb) {
    return new QuadrilateralEntity(
        parentModelId,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()),
        new VertexEmbeddable(gpb.getP4()));
  }


  private TriangleEntity createTriangleEntity(UUID parentModelId, Triangle gpb) {
    return new TriangleEntity(
        parentModelId,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()));
  }


  /**
   * @return database entity by Model UUID, Null if no corresponding Model is found
   */
  public @Nullable ModelEntity getModelById(UUID uuid) {
    Optional<ModelEntity> optionalModel = modelRepository.findById(uuid);

    if (optionalModel.isEmpty()) {
      logger.warn("Model with id: {} doesn't exist", uuid);
      return null;
    }

    return optionalModel.get();
  }

  public void insertModel(Model model) {
    modelRepository.save(createModelEntity(model, null));
  }

  //TODO, this query needs to be modified to only get model entities will null parents as thats the root model
  public List<UUID> getAllIds() {
    return modelRepository.findAllIds();
  }

}
