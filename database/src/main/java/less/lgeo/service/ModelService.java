package less.lgeo.service;

import less.lgeo.embedded.ModelId;
import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.*;
import less.lgeo.primitive.*;
import less.lgeo.repository.ColorRepository;
import less.lgeo.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service layer for interacting with {@link less.lgeo.entity.ModelEntity} from
 * the database.
 * Handles the Type conversion from {@link ModelId} to {@link Long} based on
 * {@link ModelRepository}
 */
@Service
public class ModelService {

    @Autowired
    private final ModelRepository modelRepository;

    @Autowired
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
    private ModelEntity createModelEntity(Model model, ModelEntity parent,
                                          Map<Integer, ColorEntity> colorEntityMap) {
        ModelEntity entity = new ModelEntity();
        entity.setLines(
                model.getLines().stream().map(line -> createLineEntity(entity, line, colorEntityMap)).toList());
        entity.setTriangles(
                model.getTriangles().stream()
                        .map(triangle -> createTriangleEntity(entity, triangle, colorEntityMap)).toList());
        entity.setQuadrilaterals(
                model.getQuadrilaterals().stream()
                        .map(quadrilateral -> createQuadrilateralEntity(entity, quadrilateral, colorEntityMap)).toList());
        entity.setOptionalLines(
                model.getOptionalLines().stream()
                        .map(optionalLine -> createOptionalLineEntity(entity, optionalLine, colorEntityMap)).toList());
        entity.setParent(parent);

        List<ModelEntity> childEntities = model.getPieces().stream()
                .map(subModelRef -> createModelEntity(subModelRef.getSubModel(), entity, colorEntityMap)).toList();

        entity.setChildren(childEntities);

        return entity;
    }

    private LineEntity createLineEntity(ModelEntity parentModelEntity, Line line,
                                        Map<Integer, ColorEntity> colorEntityMap) {
        LineEntity lineEntity = new LineEntity();
        lineEntity.setModel(parentModelEntity);
        lineEntity.setColor(colorEntityMap.get(line.getColorId()));
        lineEntity.setP1(new Vector3dEmbeddable(line.getP1()));
        lineEntity.setP2(new Vector3dEmbeddable(line.getP2()));
        return lineEntity;
    }

    private OptionalLineEntity createOptionalLineEntity(ModelEntity parentModelEntity,
                                                        OptionalLine optionalLine, Map<Integer, ColorEntity> colorEntityMap) {

        OptionalLineEntity optionalLineEntity = new OptionalLineEntity();
        optionalLineEntity.setModel(parentModelEntity);
        optionalLineEntity.setColor(colorEntityMap.get(optionalLine.getColorId()));
        optionalLineEntity.setP1(new Vector3dEmbeddable(optionalLine.getP1()));
        optionalLineEntity.setP2(new Vector3dEmbeddable(optionalLine.getP2()));
        optionalLineEntity.setP3(new Vector3dEmbeddable(optionalLine.getP3()));
        optionalLineEntity.setP4(new Vector3dEmbeddable(optionalLine.getP4()));
        return optionalLineEntity;
    }

    private QuadrilateralEntity createQuadrilateralEntity(ModelEntity parentModelEntity,
                                                          Quadrilateral quadrilateral, Map<Integer, ColorEntity> colorEntityMap) {
        QuadrilateralEntity quadrilateralEntity = new QuadrilateralEntity();
        quadrilateralEntity.setModel(parentModelEntity);
        quadrilateralEntity.setColor(colorEntityMap.get(quadrilateral.getColorId()));
        quadrilateralEntity.setP1(new Vector3dEmbeddable(quadrilateral.getP1()));
        quadrilateralEntity.setP2(new Vector3dEmbeddable(quadrilateral.getP2()));
        quadrilateralEntity.setP3(new Vector3dEmbeddable(quadrilateral.getP3()));
        quadrilateralEntity.setP4(new Vector3dEmbeddable(quadrilateral.getP4()));
        return quadrilateralEntity;
    }

    private TriangleEntity createTriangleEntity(ModelEntity parentModelEntity, Triangle triangle,
                                                Map<Integer, ColorEntity> colorEntityMap) {
        TriangleEntity triangleEntity = new TriangleEntity();
        triangleEntity.setModel(parentModelEntity);
        triangleEntity.setColor(colorEntityMap.get(triangle.getColorId()));
        triangleEntity.setP1(new Vector3dEmbeddable(triangle.getP1()));
        triangleEntity.setP2(new Vector3dEmbeddable(triangle.getP2()));
        triangleEntity.setP3(new Vector3dEmbeddable(triangle.getP3()));
        return triangleEntity;
    }

    /**
     * @return database entity by Model Id throws if not found
     */
    public ModelEntity getModelById(ModelId id) throws NoSuchElementException {
        return modelRepository.findById(id.getValue()).orElseThrow();
    }

    @Transactional
    public ModelId insertModel(Model model) {
        Map<Integer, ColorEntity> colorEntityMap = colorRepository.findAll().stream().collect(
                Collectors.toMap(
                        ColorEntity::getId,
                        Function.identity()));

        return modelRepository.save(createModelEntity(model, null, colorEntityMap)).getId();
    }

    public List<ModelId> getAllParentModelIds() {
        return modelRepository.findAllParentModels().stream().map(ModelEntity::getId).toList();
    }

}
