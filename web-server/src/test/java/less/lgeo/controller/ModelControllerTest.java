package less.lgeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import less.lgeo.embedded.ModelId;
import less.lgeo.embedded.Vector3dEmbeddable;
import less.lgeo.entity.ColorEntity;
import less.lgeo.entity.LineEntity;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.producer.WebServerProducer;
import less.lgeo.service.ModelService;
import less.lgeo.test.ModelTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

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
    void insert() throws Exception {
        Model cube = ModelTestUtils.cube();

        //String cubeJson = JsonFormat.printer().print(cube);
        when(webServerProducer.sendMessage(cubeJson)).thenReturn(ModelId.of(1L));

        mockMvc.perform(post("/v1/models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cubeJson))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void getModel() throws Exception {

        ModelId modelId = ModelId.of(1L);
        Vector3dEmbeddable p1 = new Vector3dEmbeddable(0, 0, 0);
        Vector3dEmbeddable p2 = new Vector3dEmbeddable(1, 1, 1);
        ColorEntity colorEntity = new ColorEntity(1, "Black", "ffffff", false, 0, 0, 0, 0);
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(modelId);
        modelEntity.setParent(null);
        LineEntity lineEntity = new LineEntity(1L, modelEntity, colorEntity, p1, p2);
        modelEntity.setLines(List.of(lineEntity));
        when(modelService.getModelById(modelId)).thenReturn(modelEntity);

        mockMvc.perform(get("/v1/models/{id}", modelId.getValue())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(modelEntity)));
    }

    @Test
    void getAllParentModelIds() throws Exception {
        List<ModelId> mockedReturn = Stream.of(1L, 2L, 3L, 4L).map(ModelId::of).toList();
        when(modelService.getAllParentModelIds()).thenReturn(mockedReturn);
        mockMvc.perform(get("/v1/models/parents/ids")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(mockedReturn)));
    }
}


