package com.bfu.catalogueservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, Object> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object convertToDatabaseColumn(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Error converting JsonNode to database column", exception);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(Object o) {
        try {
            return objectMapper.readTree((String) o);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Error converting database column to JsonNode", exception);
        }
    }
}
