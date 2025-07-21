package less.lgeo.controller;

import java.util.UUID;
import less.lgeo.producer.WebServerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping( value = "/api/model/v1" )
public class ModelController {

  private final WebServerProducer webServerProducer;

  @Autowired
  public ModelController( WebServerProducer webServerProducer ) {
    this.webServerProducer = webServerProducer;
  }

  @PostMapping( "/insert" )
  public ResponseEntity<UUID> insertModel( @RequestBody String body ) {
    UUID id = UUID.randomUUID();
    webServerProducer.sendMessage( id, body );
    return ResponseEntity.ok( id );
  }
}
