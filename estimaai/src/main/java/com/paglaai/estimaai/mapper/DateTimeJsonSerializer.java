package com.paglaai.estimaai.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.paglaai.estimaai.util.StringUtil;
import java.io.IOException;
import java.time.*;
import java.util.Date;

public class DateTimeJsonSerializer<T> extends JsonSerializer<T> {
  @Override
  public void serialize(
      T dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    if (dateTime != null) {
      if (dateTime instanceof ZonedDateTime date) {
        jsonGenerator.writeString(StringUtil.localDateTime(date.toLocalDateTime()));
      } else if (dateTime instanceof LocalDate date) {
        jsonGenerator.writeString(StringUtil.localDateTime(date.atStartOfDay()));
      } else if (dateTime instanceof LocalDateTime date) {
        jsonGenerator.writeString(StringUtil.localDateTime(date));
      } else if (dateTime instanceof OffsetDateTime date) {
        jsonGenerator.writeString(StringUtil.localDateTime(date.toLocalDateTime()));
      } else if (dateTime instanceof Instant date) {
        jsonGenerator.writeString(
            StringUtil.localDateTime(date.atOffset(ZoneOffset.UTC).toLocalDateTime()));
      } else if (dateTime instanceof Date date) {
        jsonGenerator.writeString(
            StringUtil.localDateTime(date.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime()));
      }
    }
  }
}
