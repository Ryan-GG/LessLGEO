package less.lgeo.entity;

/**
 * When we fail to get the correct data out of a repository when trying to convert to a GPB object
 */
public class EntityToGpbConversionException extends RuntimeException {

  public EntityToGpbConversionException( String message ) {
    super( message );
  }
}

