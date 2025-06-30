package com.wolfhouse.journeydaily.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wolfhouse.journeydaily.common.util.JacksonObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author linexsong
 */
@Configuration
public class ObjectMapperConfig {
    @Bean
    @Primary
    public ObjectMapper jacksonObjectMapper() {
        return getJacksonDateObjectMapper();
    }

    @Bean
    public ObjectMapper jacksonSnakeCaseObjectMapper() {
        ObjectMapper mapper = getJacksonDateObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper;
    }

    @Bean
    public ObjectMapper nonNullObjectMapper() {
        ObjectMapper mapper = getJacksonDateObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.registerModule(new JsonNullableModule());
        return mapper;
    }

    @NotNull
    private ObjectMapper getJacksonDateObjectMapper() {
        ObjectMapper objectMapper = new JacksonObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,
                                     new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                                       new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(
                                               "yyyy-MM-dd " + "HH:mm:ss")));

        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
