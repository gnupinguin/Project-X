package ru.dins.consumer;

import ru.dins.model.quote.Quote;

import java.io.Closeable;
import java.util.List;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface ProjectXConsumer extends Closeable {
    List<Quote> readQuotesFromQueue();
    void saveQuotes(List<Quote> quotes);

}
