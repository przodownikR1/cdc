package pl.java.scalatech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CdcApplication {
/*
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }*/
   @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    
    public static void main(String[] args) {
        SpringApplication.run(CdcApplication.class, args);
    }
}

