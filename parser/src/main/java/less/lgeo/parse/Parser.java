package less.lgeo.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.util.StringUtils;

public interface Parser<T> {

  T parse( File fileToParse ) throws IOException;

  File writeToFile( T gpb, String fileName );

  default List<String> read( File file ) throws IOException {
    try ( BufferedReader bufferedReader = new LineNumberReader(
        new FileReader( file, StandardCharsets.UTF_8 ) ) ) {
      return bufferedReader.lines().filter( StringUtils::hasText ).toList();
    }
  }
}
