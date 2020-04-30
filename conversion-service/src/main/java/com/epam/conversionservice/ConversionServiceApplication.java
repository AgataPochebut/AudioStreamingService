package com.epam.conversionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

//1. EnableJms + listener
//2. exporter + listener + interface
@SpringBootApplication
@EnableJms
public class ConversionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConversionServiceApplication.class, args);
	}

}
