package com.itacademy.controller.json.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.OffsetDateTime;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

  @Override
  public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    if(offsetDateTime != null) {
      jsonGenerator.writeNumber(offsetDateTime.toInstant().toEpochMilli());
    } else {
      jsonGenerator.writeNull();
    }
  }
}
