package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.ModelEntity;
import less.lgeo.entity.QuadrilateralEntity;
import less.lgeo.primitive.Quadrilateral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuadrilateralMapper implements ModelDependencyMapper<Quadrilateral, QuadrilateralEntity> {

    @Autowired
    private final ColorMapper colorMapper;

    public QuadrilateralMapper(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public Quadrilateral toDomain(QuadrilateralEntity entity) {
        Color color = colorMapper.toDomain(entity.getColor());
        return new Quadrilateral(
                color,
                entity.getP1().toDomain(),
                entity.getP2().toDomain(),
                entity.getP3().toDomain(),
                entity.getP4().toDomain()
        );
    }

    @Override
    public QuadrilateralEntity toEntity(Quadrilateral domain) {
        QuadrilateralEntity entity = new QuadrilateralEntity();
        // Don't set id, as it will be set by the sequence
        entity.setColor(colorMapper.toEntity(domain.getColor()));
        entity.setP1(new Vector3dEmbeddable(domain.getP1()));
        entity.setP2(new Vector3dEmbeddable(domain.getP2()));
        entity.setP3(new Vector3dEmbeddable(domain.getP3()));
        entity.setP4(new Vector3dEmbeddable(domain.getP4()));
        return entity;
    }

    @Override
    public QuadrilateralEntity toEntity(Quadrilateral domain, ModelEntity modelEntity) {
        QuadrilateralEntity entity = toEntity(domain);
        entity.setModel(modelEntity);
        return entity;
    }

    @Override
    public List<QuadrilateralEntity> toEntityList(List<Quadrilateral> domainList, ModelEntity modelEntity) {
        List<QuadrilateralEntity> entityList = toEntityList(domainList);
        entityList.forEach(entity -> entity.setModel(modelEntity));
        return entityList;
    }

}
