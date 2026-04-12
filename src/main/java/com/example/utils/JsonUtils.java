package com.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@Deprecated
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static <T> T readValue(Reader reader, Class<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(reader, type);
    }

    public static <T> void writeValue(Writer writer, T value) throws IOException {
        OBJECT_MAPPER.writeValue(writer, value);
    }
}
