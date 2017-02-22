package ru.dins.kafka;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.model.quote.Quote;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */
@RequiredArgsConstructor
public class QuoteSynchronizer implements Runnable {
    @NonNull
    private ProjectXProducer producer;
    @NonNull
    private ProjectXConsumer consumer;

    AtomicBoolean closed = new AtomicBoolean(false);

    @Override
    public void run() {
        try{
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                perform(quotes);
                for (Quote quote : quotes){
                    producer.addQuoteInQueue(quote);
                }
            }
        } catch (WakeupException e){
            if (!closed.get()){
                throw e;
            }
        } finally {
            System.out.println("\nQUOTE SYNC IS INTERRUPTED\n");
            consumer.close();
            producer.close();
        }
    }

    protected void perform(List<Quote> quotes){
        //nothing to do
    }


}
