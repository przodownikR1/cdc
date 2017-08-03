package pl.java.scalatech.car;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
class CarController {

    private final CarService service;

    @GetMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    List<String> cars() {
        log.info("cars body : {}", service.getCars());
        return this.service.getCars();
    }
}
