package pl.java.scalatech.car;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
@WebMvcTest(CarController.class)
public class MyCarControllerSingleTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarService carService;

    static ObjectMapper mapper = new ObjectMapper();

    static String json;
    {
        List<String> cars = Lists.newArrayList("ford", "porsche", "mercedes");
        try {
            json = mapper.writeValueAsString(cars);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void simpleMvcControllerTest() throws Exception {
        List<String> cars = Lists.newArrayList("ford", "porsche", "mercedes");
        given(this.carService.getCars()).willReturn(cars);
        this.mvc.perform(get("/cars").accept(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", is(cars.get(0))))
                .andExpect(jsonPath("$[1]", is(cars.get(1))))
                .andExpect(jsonPath("$[2]", is(cars.get(2))))
                .andExpect(content().json(json)).andDo(print());
    }

}