package ru.dins.producer;

import ru.dins.model.quote.Quote;

import java.io.Closeable;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface ProjectXProducer extends Closeable{
    void addQuoteInQueue(Quote quote);

}
