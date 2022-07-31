package com.itacademy.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itacademy.controller.utils.LocalDateDeserializer;
import com.itacademy.controller.utils.LocalDateSerializer;
import com.itacademy.controller.utils.OffsetDateTimeDeserializer;
import com.itacademy.controller.utils.OffsetDateTimeSerializer;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;


@Configuration
public class ControllerConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    JavaTimeModule javaTimeModule = new JavaTimeModule();

    javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
    javaTimeModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());

    javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer());

    mapper.registerModule(javaTimeModule);

    return mapper;
  }

  @Bean
  public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(ObjectMapper mapper) {
    MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    messageConverter.setSupportedMediaTypes(List.of(APPLICATION_JSON));
    messageConverter.setObjectMapper(mapper);
    return messageConverter;
  }

  @Bean
  public RequestMappingHandlerAdapter mappingHandlerAdapter(MappingJackson2HttpMessageConverter messageConverter) {
    RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
    requestMappingHandlerAdapter.setMessageConverters(List.of(messageConverter));
    return requestMappingHandlerAdapter;
  }

}
