package ru.dins.web.model.quote;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class QuoteSerializer implements Serializer<Quote> {
    private ObjectMapper mapper = new QuoteMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to do
    }

    @Override
    public byte[] serialize(String topic, Quote data) {
        try {
            return mapper.writerFor(Quote.class).writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when serializing Quote to byte[]", e);
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}