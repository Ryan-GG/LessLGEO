package less.lgeo.mapper;

import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.ModelEntity;
import less.lgeo.entity.OptionalLineEntity;
import less.lgeo.primitive.OptionalLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OptionalLineMapper implements ModelDependencyMapper<OptionalLine, OptionalLineEntity> {

    @Autowired
    private final ColorMapper colorMapper;

    public OptionalLineMapper(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public OptionalLine toDomain(OptionalLineEntity entity) {
        return new OptionalLine(
                colorMapper.toDomain(entity.getColor()),
                entity.getP1().toDomain(),
                entity.getP2().toDomain(),
                entity.getP3().toDomain(),
                entity.getP4().toDomain()
        );
    }

    @Override
    public OptionalLineEntity toEntity(OptionalLine domain) {
        OptionalLineEntity entity = new OptionalLineEntity();
        // Don't set id, as it will be set by the sequence
        entity.setColor(colorMapper.toEntity(domain.color()));
        entity.setP1(new Vector3dEmbeddable(domain.p1()));
        entity.setP2(new Vector3dEmbeddable(domain.p2()));
        entity.setP3(new Vector3dEmbeddable(domain.p3()));
        entity.setP4(new Vector3dEmbeddable(domain.p4()));
        return entity;
    }

    @Override
    public OptionalLineEntity toEntity(OptionalLine domain, ModelEntity modelEntity) {
        OptionalLineEntity entity = toEntity(domain);
        entity.setModel(modelEntity);
        return entity;
    }

    @Override
    public List<OptionalLineEntity> toEntityList(List<OptionalLine> domainList, ModelEntity modelEntity) {
        List<OptionalLineEntity> entityList = toEntityList(domainList);
        entityList.forEach(entity -> entity.setModel(modelEntity));
        return entityList;
    }

}
