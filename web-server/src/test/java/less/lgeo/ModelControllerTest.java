package less.lgeo;/*
package less.lgeo;

import less.lgeo.controller.ModelController;
import less.lgeo.service.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WebMvcTest(ModelController.class)
class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModelService modelService;

    @Test
    void greetingShouldReturnMessageFromService() throws Exception {
        when(modelService.getModelById(1L)).thenReturn(null);
        this.mockMvc.perform(get("/greeting"))
                .andDo(print())
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().string("foo")
                );
    }
}
*/

import less.lgeo.entity.ModelEntity;
import less.lgeo.repository.ModelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ExtendWith(TestContainersInitializer.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainersInitializer.class)
public class ModelControllerTest {
    @Autowired
    private ModelRepository repository;

    @Test
    void givenTestcontainersPostgres_whenSavePerson_thenSavedEntityShouldBeReturnedWithExpectedFields() {
        ModelEntity person = new ModelEntity();
        person.setId(1L);

        ModelEntity savedPerson = repository.save(person);
        assertNotNull(savedPerson.getId());
    }
}
