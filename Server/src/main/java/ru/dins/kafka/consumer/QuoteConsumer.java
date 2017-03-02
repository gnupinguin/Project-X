package ru.dins.kafka.consumer;

import javafx.util.Pair;
import ru.dins.web.model.quote.Quote;

import java.io.Closeable;
import java.util.List;
import java.util.Map;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface QuoteConsumer extends Closeable {
    List<Quote> readQuotesFromQueue();
    Map<Long, Quote> readQuotesFromQueueWithOffsets();
    void seek(long offset);
    void commit(long offset);

    @Override
    void close();

}
