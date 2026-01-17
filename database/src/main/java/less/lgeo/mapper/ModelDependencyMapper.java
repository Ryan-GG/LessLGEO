package less.lgeo.mapper;

import less.lgeo.entity.ModelEntity;

import java.util.List;

public interface ModelDependencyMapper<Domain, Entity> extends Mapper<Domain, Entity> {
    
    List<Entity> toEntityList(List<Domain> domainList, ModelEntity modelEntity);
}
