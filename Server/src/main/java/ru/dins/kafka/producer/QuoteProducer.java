package ru.dins.kafka.producer;

import ru.dins.web.model.quote.Quote;

/**
 * The interface determinate basic methods for sending quotes to kafka-server.
 **/
public interface QuoteProducer{
    /**
     * The method for sending a quote to local topic.
     * It's main method for sending quotes.
     * @param quote Quote for sending.
     * @exception UnsentQuoteException On error with sending quote.
     */
    void addQuote2LocalTopic(Quote quote) throws UnsentQuoteException;

    /**
     * The method for sending a quote to reserve topic.
     * Use this method, if you have some troubles with saving quote after receive a quote.
     * @param quote Quote for sending.
     * @throws UnsentQuoteException On error with sending quote.
     */
    void addQuote2ReserveTopic(Quote quote) throws UnsentQuoteException;

    /**
     * The method for sending a quote to replica topic.
     * Use this method, if you want send quote for synchronize.
     * @param quote Quote for sending.
     * @throws UnsentQuoteException On error with sending quote.
     */
    void addQuote2ReplicaTopic(Quote quote) throws UnsentQuoteException;
}
