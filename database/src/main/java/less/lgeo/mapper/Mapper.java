package less.lgeo.mapper;

import java.util.List;

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
