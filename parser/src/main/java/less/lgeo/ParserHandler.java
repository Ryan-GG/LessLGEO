package less.lgeo;

import static less.lgeo.common.CommonUtils.PART_EXT;
import static less.lgeo.common.CommonUtils.changeFileExtension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import less.lgeo.connectivity.Connection;
import less.lgeo.parse.ConnectivityParser;
import less.lgeo.parse.LDrawParser;
import less.lgeo.parser.ParserProducer;
import less.lgeo.primitive.Model;
import less.lgeo.rabbitmq.RabbitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties( value = RabbitProperties.class )
public class ParserHandler implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger( ParserHandler.class );

  private final ParserProducer parserProducer;

  public ParserHandler( ParserProducer parserProducer ) {
    this.parserProducer = parserProducer;
  }

  public static void main( String[] args ) {
    new SpringApplicationBuilder()
        .web( WebApplicationType.NONE )
        .sources( ParserHandler.class )
        .build()
        .run( args );
  }

  @Override
  public void run( ApplicationArguments args ) throws Exception {

    // TODO, this needs to be moved out to a class
    LDrawParser lDrawParser = new LDrawParser();
    ConnectivityParser connectivityParser = new ConnectivityParser();
    File fileToParse = new File( args.getSourceArgs()[0] );

    Model parentModel = lDrawParser.parse( fileToParse );

    List<Connection> connectionFiles = parentModel.getPieceList().stream()
        .map( piece -> changeFileExtension( piece.getFileName(), PART_EXT ) )
        .map( fileName -> new File( "connectivity", fileName ) )
        .map( file -> {
          try {
            return connectivityParser.parse( file );
          } catch ( IOException e ) {
            throw new RuntimeException( e );
          }
        } )
        .toList();

    logger.info( "Model result: {}", parentModel );

    logger.info( "Sending Model..." );
    parserProducer.sendMessage( parentModel );
  }
}