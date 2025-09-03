package less.lgeo.repository;

import java.util.List;
import java.util.UUID;
import less.lgeo.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link ModelEntity}
 */
@Repository
@RepositoryDefinition(domainClass = ModelEntity.class, idClass = UUID.class)
public interface ModelRepository extends JpaRepository<ModelEntity, UUID> {

  @Query(value = "SELECT id FROM models WHERE parent IS NULL", nativeQuery = true)
  List<UUID> findAllIdsWhereParentIsNull();
}
