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

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(properties = "app.baseUrl=http://localhost:6060", webEnvironment = WebEnvironment.NONE)// !
@AutoConfigureWireMock(port = 6060)
public class WiremockImportApplicationTests {
    private List<String> cars = newArrayList("ford", "porsche", "mercedes");
    private List<String> cars2 = newArrayList("ford2", "porsche2", "mercedes2");
    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().dynamicPort());

    static ObjectMapper mapper = new ObjectMapper();

    static String json;
    static String json2;
    {

        try {
            json = mapper.writeValueAsString(cars);
            json2 = mapper.writeValueAsString(cars2);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json)));
        System.err.println(this.restTemplate.getForObject("http://localhost:6060/cars", List.class));
        assertThat(this.restTemplate.getForObject("http://localhost:6060/cars", List.class)).isEqualTo(cars);
    }

    @Test
    public void shouldNotEqualBody() {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json2)));
        assertThat(this.restTemplate.getForObject("http://localhost:6060/cars", List.class)).isNotEqualTo(cars);
    }
}