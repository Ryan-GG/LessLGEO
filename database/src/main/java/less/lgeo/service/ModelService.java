package less.lgeo.service;

import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.entity.*;
import less.lgeo.primitive.*;
import less.lgeo.repository.ColorRepository;
import less.lgeo.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private ModelEntity createModelEntity(Model gpb, ModelEntity parent, Map<Integer, ColorEntity> colorEntityMap) {
        ModelEntity entity = new ModelEntity();
        entity.setLines(gpb.getLineList().stream().map(line -> createLineEntity(entity, line, colorEntityMap)).collect(Collectors.toUnmodifiableSet()));
        entity.setTriangles(
                gpb.getTriangleList().stream().map(triangle -> createTriangleEntity(entity, triangle, colorEntityMap))
                        .collect(Collectors.toUnmodifiableSet()));
        entity.setQuadrilaterals(
                gpb.getQuadrilateralList().stream()
                        .map(quadrilateral -> createQuadrilateralEntity(entity, quadrilateral, colorEntityMap)).collect(Collectors.toUnmodifiableSet()));
        entity.setOptionalLines(
                gpb.getOptionalLineList().stream()
                        .map(optionalLine -> createOptionalLineEntity(entity, optionalLine, colorEntityMap)).collect(Collectors.toUnmodifiableSet()));
        entity.setParent(parent);

        Set<ModelEntity> childEntities = gpb.getPieceList().stream()
                .map(subModelRef -> createModelEntity(subModelRef.getSubModel(), entity, colorEntityMap))
                .collect(Collectors.toUnmodifiableSet());

        entity.setChildren(childEntities);

        return entity;
    }


    private LineEntity createLineEntity(ModelEntity parentModelEntity, Line gpb, Map<Integer, ColorEntity> colorEntityMap) {
        return new LineEntity(
                UUID.randomUUID(),
                parentModelEntity,
                colorEntityMap.get(gpb.getColorId()),
                new VertexEmbeddable(gpb.getP1()),
                new VertexEmbeddable(gpb.getP2()));
    }

    private OptionalLineEntity createOptionalLineEntity(ModelEntity parentModelEntity,
                                                        OptionalLine gpb, Map<Integer, ColorEntity> colorEntityMap) {
        return new OptionalLineEntity(
                UUID.randomUUID(),
                parentModelEntity,
                colorEntityMap.get(gpb.getColorId()),
                new VertexEmbeddable(gpb.getP1()),
                new VertexEmbeddable(gpb.getP2()),
                new VertexEmbeddable(gpb.getP3()),
                new VertexEmbeddable(gpb.getP4()));
    }

    private QuadrilateralEntity createQuadrilateralEntity(ModelEntity parentModelEntity,
                                                          Quadrilateral gpb, Map<Integer, ColorEntity> colorEntityMap) {
        return new QuadrilateralEntity(
                UUID.randomUUID(),
                parentModelEntity,
                colorEntityMap.get(gpb.getColorId()),
                new VertexEmbeddable(gpb.getP1()),
                new VertexEmbeddable(gpb.getP2()),
                new VertexEmbeddable(gpb.getP3()),
                new VertexEmbeddable(gpb.getP4()));
    }


    private TriangleEntity createTriangleEntity(ModelEntity parentModelEntity, Triangle gpb, Map<Integer, ColorEntity> colorEntityMap) {
        return new TriangleEntity(
                UUID.randomUUID(),
                parentModelEntity,
                colorEntityMap.get(gpb.getColorId()),
                new VertexEmbeddable(gpb.getP1()),
                new VertexEmbeddable(gpb.getP2()),
                new VertexEmbeddable(gpb.getP3()));
    }


    /**
     * @return database entity by Model Id throws if not found
     */
    public ModelEntity getModelById(UUID uuid) throws NoSuchElementException {
        return modelRepository.findById(uuid).orElseThrow();
    }

    public UUID insertModel(Model model) {
        Map<Integer, ColorEntity> colorEntityMap = colorRepository.findAll().stream().collect(
                Collectors.toMap(
                        ColorEntity::getId,
                        Function.identity()
                )
        );

        return modelRepository.save(createModelEntity(model, null, colorEntityMap)).getId();
    }

    public List<UUID> getAllParentModelIds() {
        return modelRepository.findAllParentModelIds();
    }

}
