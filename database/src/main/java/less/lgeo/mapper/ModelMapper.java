package less.lgeo.mapper;

import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelMapper implements Mapper<Model, ModelEntity> {

    private final LineMapper lineMapper;
    private final TriangleMapper triangleMapper;
    private final QuadrilateralMapper quadrilateralMapper;
    private final OptionalLineMapper optionalLineMapper;
    private final SubFileReferenceMapper subFileReferenceMapper;

    public ModelMapper(
            LineMapper lineMapper,
            TriangleMapper triangleMapper,
            QuadrilateralMapper quadrilateralMapper,
            OptionalLineMapper optionalLineMapper,
            SubFileReferenceMapper subFileReferenceMapper
    ) {
        this.lineMapper = lineMapper;
        this.triangleMapper = triangleMapper;
        this.quadrilateralMapper = quadrilateralMapper;
        this.optionalLineMapper = optionalLineMapper;
        this.subFileReferenceMapper = subFileReferenceMapper;
    }


    @Override
    public Model toDomain(ModelEntity entity) {
        //TODO, #62 - Update Model schema to include Comments & Commands
        return new Model(
                List.of(),
                List.of(),
                lineMapper.toDomainList(entity.getLines()),
                triangleMapper.toDomainList(entity.getTriangles()),
                quadrilateralMapper.toDomainList(entity.getQuadrilaterals()),
                optionalLineMapper.toDomainList(entity.getOptionalLines()),
                subFileReferenceMapper.toDomainList(entity.getPieces(), this));
    }

    @Override
    public ModelEntity toEntity(Model domain) {
        ModelEntity modelEntity = new ModelEntity();
        //Skip Id, auto created by sequence
        modelEntity.setLines(lineMapper.toEntityList(domain.lines(), modelEntity));
        modelEntity.setTriangles(triangleMapper.toEntityList(domain.triangles(), modelEntity));
        modelEntity.setQuadrilaterals(quadrilateralMapper.toEntityList(domain.quadrilaterals(), modelEntity));
        modelEntity.setOptionalLines(optionalLineMapper.toEntityList(domain.optionalLines(), modelEntity));
        modelEntity.setPieces(subFileReferenceMapper.toEntityList(domain.pieces(), modelEntity, this));
        return modelEntity;
    }

}
