package less.lgeo.parse;

import java.io.File;
import java.io.IOException;
import less.lgeo.connectivity.Connection;
import org.springframework.stereotype.Service;

@Service
public class ConnectivityParser implements Parser<Connection> {

  @Override
  public Connection parse( File fileToParse ) throws IOException {
    return null;
  }

  @Override
  public File writeToFile( Connection gpb, String fileName ) {
    return null;
  }
}
