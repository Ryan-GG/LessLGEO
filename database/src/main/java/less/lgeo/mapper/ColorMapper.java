package less.lgeo.mapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import less.lgeo.common.Color;
import less.lgeo.entity.ColorEntity;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper implements Mapper<Color, ColorEntity> {

    @PersistenceContext
    private final EntityManager entityManager;

    public ColorMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ColorEntity toEntity(Color domain) {
        return entityManager.getReference(ColorEntity.class, domain.getId());
    }


    @Override
    public Color toDomain(ColorEntity entity) {
        return new Color(entity.getId(), entity.getName(), entity.getRgb(), entity.isTrans());
    }


}
