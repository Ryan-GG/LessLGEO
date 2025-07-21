package less.lgeo.service;

import static less.lgeo.entity.ModelEntity.toEntity;

import java.util.Optional;
import java.util.UUID;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.repository.ModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Fetches {@link less.lgeo.entity.ModelEntity} from the database
 */
@Service
public class ModelService {

  private static final Logger logger = LoggerFactory.getLogger( ModelService.class );

  private final ModelRepository modelRepository;

  public ModelService( ModelRepository modelRepository ) {
    this.modelRepository = modelRepository;
  }


  /**
   * @return database entity by Model UUID, Null if no corresponding Model is found
   */
  public @Nullable ModelEntity getModelById( UUID uuid ) {
    Optional<ModelEntity> optionalModel = modelRepository.findById( uuid.toString() );
    if ( optionalModel.isPresent() ) {
      return optionalModel.get();
    }
    logger.warn( "Model with id: {} doesn't exist", uuid );
    return null;
  }
  
  public void insertModel( Model model ) {
    modelRepository.save( toEntity( model ) );
  }
}
