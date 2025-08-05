package less.lgeo.controller;

import java.util.List;
import java.util.UUID;
import less.lgeo.primitive.Model;
import less.lgeo.producer.WebServerProducer;
import less.lgeo.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping( value = "/api/model/v1" )
public class ModelController {

  private static final Logger logger = LoggerFactory.getLogger( ModelController.class );
  @Autowired
  private final WebServerProducer webServerProducer;

  @Autowired
  private final ModelService modelService;

  public ModelController( WebServerProducer webServerProducer, ModelService modelService ) {
    this.webServerProducer = webServerProducer;
    this.modelService = modelService;
  }

  @PostMapping( "/insert" )
  public ResponseEntity<UUID> insertModel( @RequestBody String body ) {
    UUID id = UUID.randomUUID();
    webServerProducer.sendMessage( id, body );
    return ResponseEntity.ok( id );
  }

  @GetMapping( value = "/{id}", produces = "application/x-protobuf" )
  public ResponseEntity<byte[]> getModelAsJson( @PathVariable String id ) {
    Model model = modelService.getModelById( UUID.fromString( id ) );

    if ( model == null ) {
      logger.error( "Model {} was NULL", id );
      return ResponseEntity.internalServerError().body(
          ( "Failed to get model id: " + id ).getBytes() );
    }

    return ResponseEntity.ok( model.toByteArray() );
  }

  @GetMapping( "/ids" )
  public ResponseEntity<List<UUID>> getAllModelIds() {
    return ResponseEntity.ok( modelService.getAllModelUUIDs() );
  }
}
