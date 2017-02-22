package ru.dins.kafka.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import ru.dins.model.quote.Quote;

import java.io.Closeable;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface ProjectXProducer extends Closeable{
    void addQuoteInQueue(Quote quote);

    @Override
    void close();

}
