package ru.dins.kafka.synchronization;

import lombok.Data;
import lombok.NonNull;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.web.model.quote.Quote;
import ru.dins.web.service.ProjectXService;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
@Data
public class QuoteOuterSynchronizer implements Runnable{
    @NonNull
    private ProjectXConsumer consumer;
    private AtomicBoolean closed = new AtomicBoolean(false);
    @Autowired
    ProjectXService service;

    @Override
    public void run() {
        try {
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                if (quotes != null){
                    service.insertQuotes(quotes);
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) throw e;
        }finally {
            System.out.println("OUTER SYNC INTERRUPTED");
        }
    }
}
