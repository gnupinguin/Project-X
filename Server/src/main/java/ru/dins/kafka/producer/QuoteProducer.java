package ru.dins.kafka.producer;

import ru.dins.web.model.quote.Quote;

import java.io.Closeable;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface QuoteProducer extends AutoCloseable{

    void addQuote2MainPartitionLocalTopic(Quote quote);
    void addQuote2ReservePartitionLocalTopic(Quote quote);
    void addQuote2ReplicaTopic(Quote quote);

    @Override
    void close();

}
