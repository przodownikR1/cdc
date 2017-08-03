package pl.java.scalatech.car;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
class CarService {

    @Value("${app.baseUrl:http://myCar-rental.org}")
    @Getter
    @Setter
    private String base;

    public List<String> getCars() {
        List<String> cars = newArrayList("ford", "porsche", "mercedes");
        log.info("base  {}", base);
        return cars;
    }

}