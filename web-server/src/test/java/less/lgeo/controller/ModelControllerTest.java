package less.lgeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import less.lgeo.embedded.ModelId;
import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.ColorEntity;
import less.lgeo.entity.LineEntity;
import less.lgeo.entity.ModelEntity;
import less.lgeo.producer.WebServerProducer;
import less.lgeo.service.ModelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Controller test for models, use a WebMVCTest to setup a mock API server. We can make mock
 * calls to these end points and return back expected values. THe ObjectMapper allows for conversion
 * from object type to JSON which endpoints return `content()` as.
 */
@WebMvcTest(ModelController.class)
class ModelControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ModelService modelService;
    @MockitoBean
    private WebServerProducer webServerProducer;

    @Test
    void insert() {

        URL cubeResourceURL = getClass().getClassLoader().getResource("cube.ldr");
        if (cubeResourceURL == null) fail("Cube LDraw Resource doesn't exist");

        try {
            Path cubeLdrawPath = Path.of(cubeResourceURL.toURI());
            String cubeLDraw = Files.readString(cubeLdrawPath);

            when(webServerProducer.sendMessage(cubeLDraw)).thenReturn(ModelId.of(1L));

            mockMvc.perform(post("/v1/models")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(cubeLDraw))
                    .andExpect(status().isOk())
                    .andExpect(content().string("1"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void getModel() {

        ModelId modelId = ModelId.of(1L);
        Vector3dEmbeddable p1 = new Vector3dEmbeddable(0, 0, 0);
        Vector3dEmbeddable p2 = new Vector3dEmbeddable(1, 1, 1);
        ColorEntity colorEntity = new ColorEntity(1, "Black", "ffffff", false, 0, 0, 0, 0);
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(modelId);
        LineEntity lineEntity = new LineEntity(1L, modelEntity, colorEntity, p1, p2);
        modelEntity.setLines(List.of(lineEntity));
        when(modelService.getModelById(modelId)).thenReturn(Optional.of(modelEntity));

        try {
            mockMvc.perform(get("/v1/models/{id}", modelId.getValue())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(modelEntity)));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void getAllRootModelIds() {
        List<ModelId> mockedReturn = Stream.of(1L, 2L, 3L, 4L).map(ModelId::of).toList();
        when(modelService.getAllRootModelIds()).thenReturn(mockedReturn);
        try {
            mockMvc.perform(get("/v1/models/id")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(objectMapper.writeValueAsString(mockedReturn)));
        } catch (Exception e) {
            fail(e);
        }
    }
}


