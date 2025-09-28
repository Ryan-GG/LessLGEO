package less.lgeo.controller;

import less.lgeo.embedded.ModelId;
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

    private final WebServerProducer webServerProducer;
    private final ModelService modelService;

    @Autowired
    public ModelController(WebServerProducer webServerProducer, ModelService modelService) {
        this.webServerProducer = webServerProducer;
        this.modelService = modelService;
    }

    @PostMapping
    public ResponseEntity<Long> createModel(@RequestBody String body) {
        Long modelId = webServerProducer.sendMessage(body);
        return ResponseEntity.ok(modelId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelEntity> getModel(@PathVariable long id) {
        try {
            //FIXME, can path variaables be object modoeel id
            ModelEntity modelEntity = modelService.getModelById(ModelId.of(id));
            return ResponseEntity.ok(modelEntity);
        } catch (NoSuchElementException e) {
            logger.error("ModelEntity Id {} was not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/parents/ids")
    public ResponseEntity<List<ModelId>> getAllParentModelIds() {
        return ResponseEntity.ok(modelService.getAllParentModelIds());
    }
}
