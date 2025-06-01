package less.lgeo.parse;

import java.io.File;
import java.io.IOException;

public interface Parser<T> {

  T parse( File fileToParse ) throws IOException;

  File writeToFile( T gpb, String fileName );
}
