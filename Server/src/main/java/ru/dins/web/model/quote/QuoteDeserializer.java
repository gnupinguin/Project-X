package ru.dins.web.model.quote;


import org.springframework.kafka.support.serializer.JsonDeserializer;


/**
 * Class for deserialize quotes for kafka consumer.
 */
public class QuoteDeserializer extends JsonDeserializer<Quote> {
}
