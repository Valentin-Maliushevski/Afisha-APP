package com.itacademy.controller.json.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

  @Override
  public void serialize(LocalDate localDate, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    if(localDate != null) {
      jsonGenerator.writeNumber(localDate.toEpochDay());
    } else {
      jsonGenerator.writeNull();
    }
  }
}
