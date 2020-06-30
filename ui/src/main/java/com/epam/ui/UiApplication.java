package com.epam.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//ui to backend by resttemplate
//@GetMapping("/classes")
//public ResponseEntity<List<TeachingClassDto>> listClasses(){
//        return restTemplate
//        .exchange("http://"+ serviceHost +"/class", HttpMethod.GET, null,
//        new ParameterizedTypeReference<List<TeachingClassDto>>() {});
//        }
@SpringBootApplication
public class UiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }

}
