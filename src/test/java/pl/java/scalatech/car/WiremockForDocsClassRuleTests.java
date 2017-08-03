package pl.java.scalatech.car;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;



@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class WiremockForDocsClassRuleTests {
    private List<String> cars = newArrayList("ford", "porsche", "mercedes");
    @ClassRule
    public static WireMockClassRule wiremock = new WireMockClassRule(
            WireMockSpring.options().dynamicPort());

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
    private TestRestTemplate restTemplate;//!
    
    @Before
    public void setup() {
        this.service.setBase("http://localhost:" + wiremock.port());
    }

    @Autowired
    private CarService service;

    @Test
    public void contextLoads() throws Exception {

        wiremock.stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json)));
        assertThat(this.restTemplate.getForObject("/cars", List.class)).isEqualTo(cars);
    }

}