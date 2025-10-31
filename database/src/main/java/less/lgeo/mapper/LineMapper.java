package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.LineEntity;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Line;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LineMapper implements ModelDependencyMapper<Line, LineEntity> {

    @Autowired
    private final ColorMapper colorMapper;

    public LineMapper(ColorMapper colorMapper) {
        this.colorMapper = colorMapper;
    }

    @Override
    public Line toDomain(LineEntity entity) {
        Color color = colorMapper.toDomain(entity.getColor());
        return new Line(
                color,
                entity.getP1().toDomain(),
                entity.getP2().toDomain()
        );
    }

    @Override
    public LineEntity toEntity(Line domain) {
        LineEntity entity = new LineEntity();
        // Don't set id, as it will be set by the sequence
        entity.setColor(colorMapper.toEntity(domain.color()));
        entity.setP1(new Vector3dEmbeddable(domain.p1()));
        entity.setP2(new Vector3dEmbeddable(domain.p2()));
        return entity;
    }

    @Override
    public LineEntity toEntity(Line domain, ModelEntity modelEntity) {
        LineEntity entity = toEntity(domain);
        entity.setModel(modelEntity);
        return entity;
    }

    @Override
    public List<LineEntity> toEntityList(List<Line> domainList, ModelEntity modelEntity) {
        List<LineEntity> entityList = toEntityList(domainList);
        entityList.forEach(entity -> entity.setModel(modelEntity));
        return entityList;
    }

}
