package less.lgeo.mapper;

import less.lgeo.entity.ModelEntity;

import java.util.List;

public interface ModelDependencyMapper<Domain, Entity> extends Mapper<Domain, Entity> {

    Entity toEntity(Domain domain, ModelEntity modelEntity);

    List<Entity> toEntityList(List<Domain> domainList, ModelEntity modelEntity);
}
