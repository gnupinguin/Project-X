package ru.dins.kafka.synchronization;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.service.ProjectXService;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
@Data
@Service
public class QuoteLocalSynchronizer implements Runnable {
    @NonNull
    private ProjectXProducer producer;
    @NonNull
    private ProjectXConsumer consumer;
    @Getter @Setter
    private AtomicBoolean closed = new AtomicBoolean(false);
    @Autowired
    private ProjectXService service;

    @Override
    public void run() {
        try{
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                if (quotes != null){
                    service.insertQuotes(quotes);
                    for (Quote quote : quotes){
                        producer.addQuoteInQueue(quote);
                    }
                }

            }
        } catch (WakeupException e){
            if (!closed.get()){
                throw e;
            }
        } finally {
            System.err.println("\nLOCAL SYNC IS INTERRUPTED\n");
            consumer.close();
            producer.close();
        }
    }


}
