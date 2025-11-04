package less.lgeo.mapper;

import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper implements Mapper<Color, ColorEntity> {

    @Autowired
    private final ColorService colorService;

    public ColorMapper(ColorService colorService) {
        this.colorService = colorService;
    }

    @Override
    public ColorEntity toEntity(Color domain) {
        return colorService.getColorByCode(domain.getId()).orElseThrow();
    }


    @Override
    public Color toDomain(ColorEntity entity) {
        return new Color(entity.getId(), entity.getName(), entity.getRgb(), entity.isTrans());
    }


}
