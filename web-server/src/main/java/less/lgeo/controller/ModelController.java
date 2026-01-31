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
    public ResponseEntity<ModelId> insertModel(@RequestBody String body) {
        ModelId modelId = webServerProducer.sendMessage(body);
        return ResponseEntity.ok(modelId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelEntity> getModel(@PathVariable("id") Long modelId) {
        return modelService.getModelById(ModelId.of(modelId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/ids")
    public ResponseEntity<List<ModelId>> getAllRootModelIds() {
        return ResponseEntity.ok(modelService.getAllRootModelIds());
    }
}
