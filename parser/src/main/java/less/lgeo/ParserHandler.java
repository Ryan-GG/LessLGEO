package less.lgeo;

import java.io.File;
import java.io.IOException;
import less.lgeo.parse.Parser;
import less.lgeo.set.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParserHandler {

  private static final Logger logger = LoggerFactory.getLogger(ParserHandler.class);

  public static void main(String[] args) throws IOException {
    Parser parser = new Parser();
    File fileToParse = new File(args[0]);

    Model model = parser.parse(fileToParse);

    logger.info("Model result: {}", model.toString());
  }

}