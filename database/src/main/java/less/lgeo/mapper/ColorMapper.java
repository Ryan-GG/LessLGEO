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
        return colorService.getColorByCode(domain.id()).orElseThrow();
    }

    @Override
    public Color toDomain(ColorEntity entity) {
        java.awt.Color color = java.awt.Color.decode("#%s".formatted(entity.getRgb()));
        return new Color(entity.getId(), entity.getName(), color.getRed(), color.getGreen(), color.getBlue(), entity.isTrans());
    }
}
