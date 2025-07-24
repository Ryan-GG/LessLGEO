package less.lgeo.repository;

import java.util.UUID;
import less.lgeo.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link ModelEntity}
 */
@Repository
@RepositoryDefinition(domainClass = ModelEntity.class, idClass = UUID.class)
public interface ModelRepository extends JpaRepository<ModelEntity, UUID> {

}
