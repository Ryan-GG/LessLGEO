package less.lgeo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import less.lgeo.entity.ColorEntity;
import less.lgeo.service.ColorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ColorController.class)
class ColorControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ColorService colorService;

    private static String toHexString(Color color) {
        return String.format("%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        );
    }

    @Test
    void getColor() throws Exception {

        int id = 1;
        ColorEntity colorEntity = createRandomColorEntity(id);

        when(colorService.getColorByCode(id)).thenReturn(Optional.of(colorEntity));

        mockMvc.perform(get("/v1/colors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(colorEntity)));
    }

    @Test
    void getAllColors() throws Exception {

        List<ColorEntity> colorEntities = IntStream.range(0, 100).mapToObj(this::createRandomColorEntity).toList();


        when(colorService.getAllColors()).thenReturn(colorEntities);

        mockMvc.perform(get("/v1/colors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(colorEntities)));
    }

    private ColorEntity createRandomColorEntity(int id) {
        Random random = new Random();

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        Color color = new Color(r, g, b);
        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setId(id);
        colorEntity.setName(String.valueOf(id));
        colorEntity.setRgb(toHexString(color));
        colorEntity.setTrans(random.nextBoolean());
        colorEntity.setStartYear(random.nextInt());
        colorEntity.setEndYear(random.nextInt());
        colorEntity.setNumSets(random.nextInt());
        colorEntity.setNumParts(random.nextInt());
        return colorEntity;
    }


}


