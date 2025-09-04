package less.lgeo.service;

import java.util.List;
import java.util.NoSuchElementException;
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
        gpb.getLineList().stream().map(line -> createLineEntity(entity, line)).toList());
    entity.setTriangles(
        gpb.getTriangleList().stream().map(triangle -> createTriangleEntity(entity, triangle))
            .toList());
    entity.setQuadrilaterals(
        gpb.getQuadrilateralList().stream()
            .map(quadrilateral -> createQuadrilateralEntity(entity, quadrilateral)).toList());
    entity.setOptionalLines(
        gpb.getOptionalLineList().stream()
            .map(optionalLine -> createOptionalLineEntity(entity, optionalLine)).toList());
    entity.setParent(parent);

    // Recursively convert children
    List<ModelEntity> childEntities = gpb.getPieceList().stream()
        .map(subModelRef -> createModelEntity(subModelRef.getSubModel(), entity))
        .toList();
    entity.setChildren(childEntities);

    return entity;
  }


  private LineEntity createLineEntity(ModelEntity parentModelEntity, Line gpb) {
    return new LineEntity(
        UUID.randomUUID(),
        parentModelEntity,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()));
  }

  private OptionalLineEntity createOptionalLineEntity(ModelEntity parentModelEntity,
      OptionalLine gpb) {
    return new OptionalLineEntity(
        UUID.randomUUID(),
        parentModelEntity,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()),
        new VertexEmbeddable(gpb.getP4()));
  }

  private QuadrilateralEntity createQuadrilateralEntity(ModelEntity parentModelEntity,
      Quadrilateral gpb) {
    return new QuadrilateralEntity(
        UUID.randomUUID(),
        parentModelEntity,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()),
        new VertexEmbeddable(gpb.getP4()));
  }


  private TriangleEntity createTriangleEntity(ModelEntity parentModelEntity, Triangle gpb) {
    return new TriangleEntity(
        UUID.randomUUID(),
        parentModelEntity,
        colorRepository.findById(gpb.getColorId()).orElseThrow(),
        new VertexEmbeddable(gpb.getP1()),
        new VertexEmbeddable(gpb.getP2()),
        new VertexEmbeddable(gpb.getP3()));
  }


  /**
   * @return database entity by Model UUID throws if not found
   */
  public @Nullable ModelEntity getModelById(UUID uuid) throws NoSuchElementException {
    return modelRepository.findById(uuid).orElseThrow();
  }

  public void insertModel(Model model) {
    modelRepository.save(createModelEntity(model, null));
  }

  //TODO, this query needs to be modified to only get model entities will null parents as thats the root model
  public List<UUID> getAllIds() {
    return modelRepository.findAllIds();
  }

}
