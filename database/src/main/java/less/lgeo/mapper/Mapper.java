package less.lgeo.mapper;

import java.util.List;

/**
 * Defines mapping logic between the domain model(POJO) and a database entity object
 *
 * @param <Domain> POJO, See - {@link less.lgeo.primitive.Quadrilateral}, {@link less.lgeo.primitive.Triangle}
 * @param <Entity> Database object, See -  {@link less.lgeo.entity.QuadrilateralEntity},{@link less.lgeo.entity.TriangleEntity}
 */
public interface Mapper<Domain, Entity> {

    Domain toDomain(Entity entity);

    Entity toEntity(Domain domain);

    default List<Domain> toDomainList(List<Entity> entityList) {
        return entityList.stream().map(this::toDomain).toList();
    }

    default List<Entity> toEntityList(List<Domain> domainList) {
        return domainList.stream().map(this::toEntity).toList();
    }
}
