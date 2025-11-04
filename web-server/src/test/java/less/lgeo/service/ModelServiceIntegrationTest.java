package less.lgeo.service;

import less.lgeo.PostgresTestInitializer;
import less.lgeo.embedded.ModelId;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.test.ModelTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * Corresponds to Integration to {@link ModelService} rather than having the test in Database,
 * Spring needs access to an Application Context respective to a main class. In this case the
 * {@link less.lgeo.WebServer} is the main interaction with the database layer and thus will hold
 * integration tests for it.
 * </p>
 * <p>
 * `application-test.yaml` configures setup for Spring bean creation and allows us to make
 * modification to logging / configuration of tests. This should be kept in sync with the main
 * `application.yaml`
 * </p>
 *
 * @author Ryan Gross
 */
@SpringBootTest(
        classes = less.lgeo.WebServer.class,
        properties = "spring.config.location=classpath:/application-test.yaml"
)
@ExtendWith(value = {PostgresTestInitializer.class})
@ContextConfiguration(initializers = PostgresTestInitializer.class)
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

        assertEquals(List.of(cubeId, nestedCubesId), modelService.getAllRootModelIds());
    }

}
