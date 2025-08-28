package less.lgeo.controller;

import java.util.List;
import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API endpoints for {@link ColorEntity} CRUD operations
 */
@Validated
@RestController
@RequestMapping( value = "/v1/colors" )
public class ColorController {

  @Autowired
  private final ColorService colorService;

  public ColorController( ColorService colorService ) {
    this.colorService = colorService;
  }

  @GetMapping( "/" )
  public ResponseEntity<List<ColorEntity>> getAllColors() {
    return ResponseEntity.ok().body( colorService.getAllColors() );
  }

  @GetMapping( "/{id}" )
  public ResponseEntity<ColorEntity> getColor( @PathVariable int id ) {
    return ResponseEntity.ok().body( colorService.getColorByCode( id ) );
  }

  @DeleteMapping( "/{id}" )
  public ResponseEntity<Void> deleteColor( @PathVariable int id ) {
    colorService.deleteColorById( id );
    return ResponseEntity.ok().build();
  }

}
