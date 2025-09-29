package less.lgeo.repository;

import less.lgeo.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for {@link ModelEntity}
 */
@Repository
@RepositoryDefinition(domainClass = ModelEntity.class, idClass = Long.class)
public interface ModelRepository extends JpaRepository<ModelEntity, Long> {

    @Query(value = "SELECT * FROM models WHERE parent_id IS NULL", nativeQuery = true)
    List<ModelEntity> findAllParentModels();
}
