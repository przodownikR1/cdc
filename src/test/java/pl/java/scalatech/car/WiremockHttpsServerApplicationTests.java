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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureHttpClient;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;


@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(properties="app.baseUrl=https://localhost:8443")
@AutoConfigureHttpClient
@Ignore
public class WiremockHttpsServerApplicationTests {
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

    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().httpsPort(8443));

    @Autowired
    private CarService service;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        stubFor(get(urlEqualTo("/cars")).willReturn(aResponse()
                .withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json)));
        assertThat(this.restTemplate.getForObject("https://localhost:8443/cars", List.class)).isEqualTo(cars);
    }

}