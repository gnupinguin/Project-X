package ru.dins.kafka.synchronization;

import com.mongodb.DBCollection;
import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.kafka.consumer.ProjectXConsumer;
import ru.dins.kafka.producer.ProjectXProducer;
import ru.dins.model.quote.Quote;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by gnupinguin on 22.02.17.
 */


public class QuoteLocalSynchronizer extends Synchronizer {
    private ProjectXProducer producer;
    private ProjectXConsumer consumer;
    @Getter @Setter
    private AtomicBoolean closed = new AtomicBoolean(false);

    public QuoteLocalSynchronizer(ProjectXProducer producer, ProjectXConsumer consumer, DBCollection quotesDbCollection) {
        super(quotesDbCollection);
        this.producer = producer;
        this.consumer = consumer;
    }

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
            System.out.println("\nLOCAL SYNC IS INTERRUPTED\n");
            consumer.close();
            producer.close();
        }
    }


}
