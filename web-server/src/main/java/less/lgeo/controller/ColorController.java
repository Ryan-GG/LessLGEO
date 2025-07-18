package less.lgeo.controller;

import java.util.List;
import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping(value = "/api/color/v1")
public class ColorController {

  private static final Logger logger = LoggerFactory.getLogger(ColorController.class);

  @Autowired
  private final ColorService colorService;

  public ColorController(ColorService colorService) {
    this.colorService = colorService;
  }

  @GetMapping("/")
  public ResponseEntity<List<ColorEntity>> getAllColors() {
    return ResponseEntity.ok().body(colorService.getAllColors());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ColorEntity> getColor(@PathVariable Integer id) {
    return ResponseEntity.ok().body(colorService.getColorByCode(id));
  }

  @PostMapping("/insert")
  public ResponseEntity<Void> insertColor(@RequestBody String colorMessage) {
    //TODO, RDG
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteColor(@PathVariable Integer id) {
    colorService.deleteColorById(id);
    return ResponseEntity.ok().build();
  }

}
