package pl.java.scalatech.car;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationCarTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
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
    public void shouldReturnRightResponse() {
        ResponseEntity<String> result = restTemplate.getForEntity("/cars", String.class);
        then(result.getBody()).isEqualTo(json);
        then(result.getStatusCodeValue()).isEqualTo(200);
    }

}
