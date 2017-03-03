package ru.dins.kafka.synchronization;

import lombok.*;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.dins.kafka.consumer.QuoteConsumer;
import ru.dins.kafka.producer.QuoteProducer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.persistence.QuoteRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
@Service @NoArgsConstructor
public class QuoteInnerSynchronizer implements Runnable {
    @Autowired
    private QuoteProducer producer;

    @Autowired @Qualifier("innerMainPartitionLocalTopicConsumerFromFile")
    private QuoteConsumer mainPartitionConsumer;

    @Autowired @Qualifier("innerReservePartitionLocalTopicConsumerFromFile")
    private QuoteConsumer reservePartitionConsumer;

    private AtomicBoolean closed = new AtomicBoolean(false);

    @Autowired
    private QuoteRepository repository;

    /*
    * It is bad realisation for safe-data.
    * If you want a productive function, then you should first try to write all new(or old) quotes to the repository.
    * If this action did not have bugs, you can send the old(new) one quotes(must be max.poll.records = 1)
    *
    * */
    @Override
    public void run() {
        try{
            while (!closed.get()){
                List<Quote> quotes = mainPartitionConsumer.readQuotesFromQueue();
                if (quotes != null){
                    for (Quote quote : quotes) {
                        if (repository.availableConnection()){
                            repository.addQuote(quote);
                        }else{
                            producer.addQuote2ReservePartitionLocalTopic(quote);
                        }
                        producer.addQuote2ReplicaTopic(quote);
                    }
                }
                if (repository.availableConnection()){
                    List<Quote> nonReceivedQuotes = reservePartitionConsumer.readQuotesFromQueue();
                    if (nonReceivedQuotes != null){
                        for (Quote quote : nonReceivedQuotes) {
                            if (repository.availableConnection()){
                                repository.addQuote(quote);
                            }else{
                                producer.addQuote2ReservePartitionLocalTopic(quote);
                            }
                        }
                    }
                }
            }
        } catch (WakeupException e){
            if (!closed.get()){
                throw e;
            }
        } finally {
            System.err.println("\nLOCAL SYNC IS INTERRUPTED\n");
            mainPartitionConsumer.close();
            producer.close();
        }

    }
}
