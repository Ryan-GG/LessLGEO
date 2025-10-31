package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper implements Mapper<Color, ColorEntity> {

    @Override
    public Color toDomain(ColorEntity entity) {
        return new Color(entity.getId(), entity.getName(), entity.getRgb(), entity.isTrans());
    }

    @Override
    public ColorEntity toEntity(Color domain) {
        //Only sets Id for joining
        ColorEntity entity = new ColorEntity();
        entity.setId(domain.getId());
        return entity;
    }

}
