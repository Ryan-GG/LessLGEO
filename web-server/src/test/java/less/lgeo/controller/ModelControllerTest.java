package less.lgeo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import java.util.List;
import less.lgeo.embedded.VertexEmbeddable;
import less.lgeo.entity.ColorEntity;
import less.lgeo.entity.LineEntity;
import less.lgeo.entity.ModelEntity;
import less.lgeo.primitive.Model;
import less.lgeo.producer.WebServerProducer;
import less.lgeo.service.ModelService;
import less.lgeo.test.ModelTestUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * API Controller test for models, use a WebMVCTest to setup a mock API server. We can make mock
 * calls to these end points and return back expected values. THe ObjectMapper allows for conversion
 * from object type to JSON which endpoints return `content()` as.
 */
@Nested
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
    Model cube = ModelTestUtils.cube().build();

    String cubeJson = JsonFormat.printer().print(cube);
    when(webServerProducer.sendMessage(cubeJson)).thenReturn(1L);

    mockMvc.perform(post("/v1/models/insert")
            .contentType("application/json")
            .content(cubeJson))
        .andExpect(status().isOk())
        .andExpect(content().string("1"));
  }

  @Test
  void getModel() throws Exception {

    VertexEmbeddable p1 = new VertexEmbeddable(0, 0, 0);
    VertexEmbeddable p2 = new VertexEmbeddable(1, 1, 1);
    ColorEntity colorEntity = new ColorEntity(1, "Black", "ffffff", false, 0, 0, 0, 0);
    ModelEntity modelEntity = new ModelEntity();
    modelEntity.setId(1L);
    modelEntity.setParent(null);
    LineEntity lineEntity = new LineEntity(1L, modelEntity, colorEntity, p1, p2);
    modelEntity.setLines(List.of(lineEntity));
    when(modelService.getModelById(1L)).thenReturn(modelEntity);

    mockMvc.perform(get("/v1/models/{id}", 1)
            .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(modelEntity)));
  }

  @Test
  void getAllParentModelIds() throws Exception {
    List<Long> mockedReturn = List.of(1L, 2L, 3L, 4L);
    when(modelService.getAllParentModelIds()).thenReturn(mockedReturn);
    mockMvc.perform(get("/v1/models/ids")
            .contentType("application/json"))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(mockedReturn)));
  }
}


