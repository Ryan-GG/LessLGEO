package less.lgeo.controller;

import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API endpoints for {@link ColorEntity} CRUD operations
 */
@Validated
@RestController
@RequestMapping(value = "/v1/colors")
public class ColorController {

    @Autowired
    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping
    public ResponseEntity<List<ColorEntity>> getAllColors() {
        return ResponseEntity.ok().body(colorService.getAllColors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorEntity> getColor(@PathVariable int id) {
        return ResponseEntity.ok().body(colorService.getColorByCode(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable int id) {
        colorService.deleteColorById(id);
        return ResponseEntity.ok().build();
    }
}
