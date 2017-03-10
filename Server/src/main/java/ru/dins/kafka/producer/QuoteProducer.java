package ru.dins.kafka.producer;

import ru.dins.web.model.quote.Quote;

/**
 * Created by gnupinguin on 20.02.17.
 */
public interface QuoteProducer{

    void addQuote2LocalTopic(Quote quote) throws UnsentQuoteException;
    void addQuote2ReserveTopic(Quote quote) throws UnsentQuoteException;
    void addQuote2ReplicaTopic(Quote quote) throws UnsentQuoteException;
}
