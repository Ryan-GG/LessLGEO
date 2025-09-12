package less.lgeo.controller;

import less.lgeo.entity.ModelEntity;
import less.lgeo.producer.WebServerProducer;
import less.lgeo.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST API endpoints for {@link ModelController} CRUD operations
 */
@Validated
@RestController
@RequestMapping(value = "/v1/models")
public class ModelController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);
    @Autowired
    private final WebServerProducer webServerProducer;

    @Autowired
    private final ModelService modelService;

    public ModelController(WebServerProducer webServerProducer, ModelService modelService) {
        this.webServerProducer = webServerProducer;
        this.modelService = modelService;
    }

    @PostMapping("/insert")
    public ResponseEntity<Long> insertModel(@RequestBody String body) {
        return ResponseEntity.ok(webServerProducer.sendMessage(body));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ModelEntity> getModel(@PathVariable String id) {
        try {
            ModelEntity modelEntity = modelService.getModelById(Long.parseLong(id));
            return ResponseEntity.ok(modelEntity);
        } catch (NoSuchElementException e) {
            logger.error("ModelEntity Id {} was not found", id);
            return ResponseEntity.internalServerError().body(new ModelEntity());
        }
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Integer>> getAllModelIds() {
        return ResponseEntity.ok(modelService.getAllParentModelIds());
    }
}
