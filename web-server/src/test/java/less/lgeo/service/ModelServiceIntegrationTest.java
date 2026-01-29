package less.lgeo.service;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.RefreshMode;
import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.util.ModelTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * Corresponds to Integration to {@link ModelService} rather than having the
 * test in Database,
 * Spring needs access to an Application Context respective to a main class. In
 * this case the
 * {@link less.lgeo.WebServer} is the main interaction with the database layer
 * and thus will hold
 * integration tests for it.
 * </p>
 * <p>
 * `application-test.yaml` configures setup for Spring bean creation and allows
 * us to make
 * modification to logging / configuration of tests. This should be kept in sync
 * with the main
 * `application.yaml`
 * </p>
 *
 * @author Ryan Gross
 */
@SpringBootTest(classes = less.lgeo.WebServer.class, properties = "spring.config.location=classpath:/application-test.yaml")
@AutoConfigureEmbeddedDatabase(refresh = RefreshMode.AFTER_EACH_TEST_METHOD)
public class ModelServiceIntegrationTest {

    @Autowired
    private ModelService modelService;

    @Test
    void saveAndRetrieveModel() {

        Model model = ModelTestUtils.cube();
        ModelId id = modelService.insertModel(model);

        ModelEntity retrievedEntity = modelService.getModelById(id).orElseThrow();
        assertNotNull(retrievedEntity);
        assertEquals(6, retrievedEntity.getQuadrilaterals().size());
    }

    @Test
    void getParentModelIds() {
        ModelId cubeId = modelService.insertModel(ModelTestUtils.cube());
        ModelId nestedCubesId = modelService.insertModel(ModelTestUtils.nestedCubes());

        assertTrue(List.of(cubeId, nestedCubesId).containsAll(modelService.getAllRootModelIds()));
    }

}
