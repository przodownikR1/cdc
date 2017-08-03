package pl.java.scalatech.car;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

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


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE)
@DirtiesContext
public class WiremockMockServerApplicationTests {
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
				.baseUrl("http://example.org") //
				.stubs("classpath:/stubs/getCars.json").build();
		assertThat(this.service.getCars()).isEqualTo(cars);
		server.verify();
	}

}