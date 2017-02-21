package ru.dins.consumer;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.apache.kafka.common.errors.WakeupException;
import ru.dins.model.quote.Quote;
import ru.dins.producer.ProjectXProducer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by gnupinguin on 16.02.17.
 */
@AllArgsConstructor
public class LocalTopicConsumer implements Runnable{
    @NonNull
    private ProjectXProducer producerInnerReplica;

    @NonNull
    private ProjectXProducer producerOuterReplica;

    @NonNull
    private ProjectXConsumer consumer;

    private final AtomicBoolean closed = new AtomicBoolean(false);

    public void run(){
        try{
            while (!closed.get()){
                List<Quote> quotes = consumer.readQuotesFromQueue();
                for (Quote quote : quotes){
                    producerInnerReplica.addQuoteInQueue(quote);
                    producerOuterReplica.addQuoteInQueue(quote);
                }
            }
        }catch (WakeupException e){
            if (!closed.get()) throw e;
        } finally {
            System.out.println("\nLocalTopicConsumer was interrupted\n");
            producerInnerReplica.close();
            producerOuterReplica.close();
            consumer.close();
        }
    }

}
