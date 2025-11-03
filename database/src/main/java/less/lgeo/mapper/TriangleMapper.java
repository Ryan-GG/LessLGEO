package less.lgeo.mapper;

import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.ModelEntity;
import less.lgeo.entity.TriangleEntity;
import less.lgeo.primitive.Triangle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TriangleMapper implements ModelDependencyMapper<Triangle, TriangleEntity> {

    @Autowired
    private final ColorMapper colorMapper;

    public TriangleMapper(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public Triangle toDomain(TriangleEntity entity) {
        return new Triangle(
                colorMapper.toDomain(entity.getColor()),
                entity.getP1().toDomain(),
                entity.getP2().toDomain(),
                entity.getP3().toDomain()
        );
    }

    @Override
    public TriangleEntity toEntity(Triangle domain) {
        TriangleEntity entity = new TriangleEntity();
        // Don't set id, as it will be set by the sequence
        entity.setColor(colorMapper.toEntity(domain.color()));
        entity.setP1(new Vector3dEmbeddable(domain.p1()));
        entity.setP2(new Vector3dEmbeddable(domain.p2()));
        entity.setP3(new Vector3dEmbeddable(domain.p3()));
        return entity;
    }

    @Override
    public TriangleEntity toEntity(Triangle domain, ModelEntity modelEntity) {
        TriangleEntity entity = toEntity(domain);
        entity.setModel(modelEntity);
        return entity;
    }

    @Override
    public List<TriangleEntity> toEntityList(List<Triangle> domainList, ModelEntity modelEntity) {
        List<TriangleEntity> entityList = toEntityList(domainList);
        entityList.forEach(entity -> entity.setModel(modelEntity));
        return entityList;
    }

}
