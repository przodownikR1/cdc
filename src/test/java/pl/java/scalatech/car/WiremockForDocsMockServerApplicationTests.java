package pl.java.scalatech.car;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.WireMockRestServiceServer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class WiremockForDocsMockServerApplicationTests {
    private List<String> cars = newArrayList("ford", "porsche", "mercedes");
    static ObjectMapper mapper = new ObjectMapper();
    static String json;
    {
        try {
            json = mapper.writeValueAsString(cars);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CarService service;

    @Test
    @Ignore
    public void contextLoads() throws Exception {

        MockRestServiceServer server = WireMockRestServiceServer.with(this.restTemplate)
                .baseUrl("http://myCar-rental.org/").stubs("classpath:/stubs/**/*.json").build();

        // assertThat(this.restTemplate.getForObject("/cars", List.class))
        // .isEqualTo(json);
        // assertThat(mapper.writeValueAsString(this.service.getCars())).isEqualTo(json);
        // server.verify();
    }
}