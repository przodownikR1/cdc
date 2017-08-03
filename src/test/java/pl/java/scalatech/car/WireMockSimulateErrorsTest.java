package pl.java.scalatech.car;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.apache.http.NoHttpResponseException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;


//NONE will only create spring beans not any mock servlet environment.

//MOCK will create spring beans and a mock servlet environment.

//RANDOM_PORT will start the actual Servlet container on a random port, this can be autowired using the @LocalServerPort.

//@LocalServerPort
//protected int serverPort;

//DEFINED_PORT will take the defined port in the properties and start the server on them. The default is RANDOM_PORT when u dont define any webEnvironment. 

@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(properties = "app.baseUrl=http://localhost:6061", webEnvironment = WebEnvironment.NONE)//?
public class WireMockSimulateErrorsTest {

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
            WireMockSpring.options().port(6061));

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Autowired
    private CarService service;
    
    private TestRestTemplate restTemplate = new TestRestTemplate();//?

    @Test
    public void getCars() throws Exception {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withHeader(CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE).withBody(json)));
        assertThat(this.service.getCars()).isEqualTo(cars);
        assertThat(this.restTemplate.getForObject("http://localhost:6061/cars", List.class)).isEqualTo(cars);//?
    }

    @Test
    public void randomData() throws Exception {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        expected.expectCause(instanceOf(Exception.class));
        assertThat(this.service.getCars()).isEqualTo(cars);
        assertThat(this.restTemplate.getForObject("http://localhost:6061/cars", List.class)).isEqualTo(cars);
    }

    @Test
    public void emptyResponse() throws Exception {
        stubFor(get(urlEqualTo("/cars"))
                .willReturn(aResponse().withFault(Fault.EMPTY_RESPONSE)));
        expected.expectCause(instanceOf(NoHttpResponseException.class));
        assertThat(this.service.getCars()).isEqualTo(cars);
        assertThat(this.restTemplate.getForObject("http://localhost:6061/cars", List.class)).isEqualTo(cars);
    }

    
    
}
