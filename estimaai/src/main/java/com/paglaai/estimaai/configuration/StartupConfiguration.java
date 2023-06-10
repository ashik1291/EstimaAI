package com.paglaai.estimaai.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.paglaai.estimaai.mapper.DateTimeJsonSerializer;
import java.time.*;
import java.util.Date;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupConfiguration {

  @Bean
  public ObjectMapper createObjectMapper() {
    return objectMapper();
  }

  public static ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(Date.class, new DateTimeJsonSerializer<>());
    module.addSerializer(Instant.class, new DateTimeJsonSerializer<>());
    module.addSerializer(LocalDate.class, new DateTimeJsonSerializer<>());
    module.addSerializer(LocalDateTime.class, new DateTimeJsonSerializer<>());
    module.addSerializer(ZonedDateTime.class, new DateTimeJsonSerializer<>());
    module.addSerializer(OffsetDateTime.class, new DateTimeJsonSerializer<>());

    module.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
    module.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
    module.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
    module.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);
    module.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);

    objectMapper.registerModule(module);
    return objectMapper;
  }
}
