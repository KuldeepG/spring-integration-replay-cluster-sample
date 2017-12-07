package com.github.kuldeepg.query.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class SerializerConfiguration {
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    return objectMapper;
  }

  @Bean
  public MessageConverter messageConverter(ObjectMapper objectMapper) {
    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
    messageConverter.setTypeIdPropertyName("eventType");
    messageConverter.setTargetType(MessageType.TEXT);
    messageConverter.setObjectMapper(objectMapper);
    return messageConverter;
  }
}
