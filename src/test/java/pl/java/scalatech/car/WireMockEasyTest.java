package pl.java.scalatech.car;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "app.baseUrl=http://localhost:6060", webEnvironment = WebEnvironment.NONE)
@AutoConfigureWireMock(port = 6060)
public class WireMockEasyTest {
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
    private CarService service;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json)));
        assertThat(this.restTemplate.getForObject("http://localhost:6060/cars", List.class)).isEqualTo(cars);
        assertThat(service.getCars()).isEqualTo(cars);

    }

    @Test
    public void integrationTest() {

    }

}