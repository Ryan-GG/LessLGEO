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
@RequestMapping(value = "/api/parse/v1")
public class ParseController {

  private final WebServerProducer webServerProducer;

  @Autowired
  public ParseController(WebServerProducer webServerProducer) {
    this.webServerProducer = webServerProducer;
  }

  // TODO figureout this mapping
  @PostMapping("/lDraw")
  public void parseLDraw(@RequestBody String body) {
    webServerProducer.sendMessage(body);
  }
}
