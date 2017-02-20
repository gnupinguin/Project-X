package ru.dins.consumer;

import java.io.Closeable;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface ProjectXConsumer extends Closeable {
    void readQuoteFromQueue();
}
