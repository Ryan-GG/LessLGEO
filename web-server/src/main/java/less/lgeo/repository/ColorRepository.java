package less.lgeo.repository;

import less.lgeo.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for @
 */
@Repository
@RepositoryDefinition(domainClass = ColorEntity.class, idClass = Integer.class)
public interface ColorRepository extends JpaRepository<ColorEntity, Integer> {

}
