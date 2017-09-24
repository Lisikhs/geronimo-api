package com.geronimo.config;

import com.geronimo.model.converter.LocalDateTimeAttributeConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public LocalDateTimeAttributeConverter converter() {
        return new LocalDateTimeAttributeConverter();
    }

    /**
     * Model mapper that translates entities to DTOs and DTOs to entities
     * @return an instance of {@link ModelMapper}
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

}
