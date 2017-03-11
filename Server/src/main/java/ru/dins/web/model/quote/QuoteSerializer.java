package ru.dins.web.model.quote;

import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Class for serialize quotes for kafka producer.
 */
public class QuoteSerializer extends JsonSerializer<Quote> {
}