package com.epam.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.epam")
@Import({DatabaseConfiguration.class, WebConfiguration.class, MappingConfiguration.class, SwaggerConfiguration.class, SecurityConfiguration.class})
//logging in xml
public class AppConfiguration {
}
