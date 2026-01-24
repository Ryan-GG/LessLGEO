package less.lgeo.mapper;

import less.lgeo.embedded.PointEmbeddable;
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
        return new Line(
                colorMapper.toDomain(entity.getColor()),
                entity.getP1().toDomain(),
                entity.getP2().toDomain()
        );
    }

    @Override
    public LineEntity toEntity(Line domain) {
        LineEntity entity = new LineEntity();
        // Don't set id, as it will be set by the sequence
        entity.setColor(colorMapper.toEntity(domain.color()));
        entity.setP1(new PointEmbeddable(domain.p1()));
        entity.setP2(new PointEmbeddable(domain.p2()));
        return entity;
    }

    @Override
    public List<LineEntity> toEntityList(List<Line> domainList, ModelEntity modelEntity) {
        List<LineEntity> entityList = toEntityList(domainList);
        entityList.forEach(entity -> entity.setModel(modelEntity));
        return entityList;
    }

}
