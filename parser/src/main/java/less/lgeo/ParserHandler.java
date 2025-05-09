package less.lgeo;

import java.io.File;
import java.io.IOException;
import less.lgeo.parse.Parser;
import less.lgeo.set.Model;

public class ParserHandler {

  public static void main(String[] args) {
    Parser parser = new Parser();
    File fileToParse = new File(args[0]);

    try {
      Model model = parser.parse(fileToParse);

      System.out.println(model.toString());

    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

}