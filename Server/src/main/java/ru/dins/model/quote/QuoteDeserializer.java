package ru.dins.model.quote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

/**
 * Created by gnupinguin on 16.02.17.
 */
public class QuoteDeserializer implements Deserializer<Quote> {
    private ObjectMapper mapper = new QuoteMapper();

    @Override
    public void configure(Map<String, ?> map, boolean b) {
        //nothing to do
    }

    @Override
    public Quote deserialize(String s, byte[] bytes) {
        try{
            return mapper.readerFor(Quote.class).readValue(bytes);
        } catch (IOException e){
            throw new SerializationException("EXCEPTION FROM DESERIALIZATION");
        }
    }

    @Override
    public void close() {
        //nothing to do
    }
}
