package less.lgeo.set;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LDrawParser {
   // only UTF-8 encoding
   // file name maximum 255 characters long including extension
   // Special characters, such as &, #, |, and ?, should be avoided as they may also cause cross-platform issues and create problems when used in URLs.
    // Extension
    // All LDraw files carry the LDR (default), DAT or MPD extension.
    // one command per line
    // no line length restriction
    // each command optional leading whitespacce followed by white space delimtied tokens
    // trailing arbitrary data which could include internal white space, treated as a single unit
    // white space is tabs or one or more spacese
    // empty lines or consist of only whitespace are skiped
    // if a line is non-empty the first token must be an integer from the valid line type numbers
    // this number dictates the number of parsing token for that line
    // all lines in the file must usee the line termination of <CR><LF>
    // the file is permitted **but not required** to eend with <CR><LF>

    // LDraw parts are measured in LDraw Units LDU

    class Model
    {

    }
    // TODO, This should parse into some POJO which we can use to modify or do something with
    public Model parse(File toParse) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader( new FileReader( toParse, StandardCharsets.UTF_8 ) ) )
        {
            bufferedReader.lines()
        }
        return null;
    }

    private void parseCommandLine()
    {

    }

    private void parseCommand()
    {

    }

    private void parseComment()
    {

    }

    private void parseMetaCommand()
    {

    }

    private void parseSubFileReference()
    {

    }

    private void parseLine()
    {

    }

    private void parseTriangle()
    {

    }

    private void parseQuadrilateral()
    {

    }

    private void parseOptionalLine()
    {

    }

}
