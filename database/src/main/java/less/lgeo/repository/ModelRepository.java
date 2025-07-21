package less.lgeo.repository;

import less.lgeo.entity.ColorEntity;
import less.lgeo.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link ColorEntity}
 */
@Repository
@RepositoryDefinition( domainClass = ModelEntity.class, idClass = String.class )
public interface ModelRepository extends JpaRepository<ModelEntity, String> {

}
