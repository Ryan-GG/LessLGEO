package less.lgeo.controller;

import less.lgeo.producer.WebServerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/api/model/v1")
public class ModelController {

  private final WebServerProducer webServerProducer;

  @Autowired
  public ModelController(WebServerProducer webServerProducer) {
    this.webServerProducer = webServerProducer;
  }
  
  @PostMapping("/lDraw")
  public void parseLDraw(@RequestBody String body) {
    webServerProducer.sendMessage(body);
  }
}
