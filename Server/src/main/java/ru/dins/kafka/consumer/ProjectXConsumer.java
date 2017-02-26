package ru.dins.kafka.consumer;

import ru.dins.web.model.quote.Quote;

import java.io.Closeable;
import java.util.List;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface ProjectXConsumer extends Closeable {
    List<Quote> readQuotesFromQueue();

    @Override
    void close();
}
