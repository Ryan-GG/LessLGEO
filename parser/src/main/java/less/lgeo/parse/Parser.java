package less.lgeo.parse;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.List;

public interface Parser<T> {

    T parse(String toParse);

    void writeToFile(T domain, Path outputPath);

    default List<String> read(String file) {
        BufferedReader bufferedReader = new LineNumberReader(new StringReader(file));
        return bufferedReader.lines().filter(StringUtils::hasText).toList();
    }
}
