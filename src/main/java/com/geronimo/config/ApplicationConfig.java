package com.geronimo.config;

import com.geronimo.model.converter.LocalDateTimeAttributeConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public LocalDateTimeAttributeConverter converter() {
        return new LocalDateTimeAttributeConverter();
    }
}
